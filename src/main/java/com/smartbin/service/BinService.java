package com.smartbin.service;

import com.smartbin.dto.BinDTO;
import com.smartbin.dto.StatsDTO;
import com.smartbin.entity.Area;
import com.smartbin.entity.Bin;
import com.smartbin.entity.Bin.BinStatus;
import com.smartbin.repository.AreaRepository;
import com.smartbin.repository.BinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BinService {

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private AreaRepository areaRepository;

    public List<BinDTO> getAllBins() {
        List<Bin> bins = binRepository.findAllWithArea();
        return bins.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<BinDTO> getBinById(Long id) {
        return binRepository.findById(id).map(this::convertToDTO);
    }

    public Optional<BinDTO> getBinByCode(String code) {
        return binRepository.findByCode(code).map(this::convertToDTO);
    }

    public List<BinDTO> getBinsByAreaId(Long areaId) {
        List<Bin> bins = binRepository.findByAreaIdWithArea(areaId);
        return bins.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Page<BinDTO> searchBins(String search, Long areaId, BinStatus status,
            Integer levelMin, Integer levelMax,
            int page, int size, String sortBy, String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Bin> binPage = binRepository.findBinsWithFilters(search, areaId, status, levelMin, levelMax, pageable);
        return binPage.map(this::convertToDTO);
    }

    public BinDTO createBin(BinDTO binDTO) {
        if (binRepository.existsByCode(binDTO.getCode())) {
            throw new RuntimeException("Bin with code " + binDTO.getCode() + " already exists");
        }

        Area area = areaRepository.findById(binDTO.getAreaId())
                .orElseThrow(() -> new RuntimeException("Area not found with id: " + binDTO.getAreaId()));

        Bin bin = convertToEntity(binDTO);
        bin.setArea(area);

        Bin savedBin = binRepository.save(bin);
        return convertToDTO(savedBin);
    }

    public BinDTO updateBin(Long id, BinDTO binDTO) {
        Bin existingBin = binRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bin not found with id: " + id));

        // Update fields
        existingBin.setName(binDTO.getName());
        existingBin.setLevel(binDTO.getLevel());
        existingBin.setOnline(binDTO.getOnline());

        existingBin.setLatitude(binDTO.getLatitude());
        existingBin.setLongitude(binDTO.getLongitude());

        if (binDTO.getAreaId() != null && !binDTO.getAreaId().equals(existingBin.getArea().getId())) {
            Area newArea = areaRepository.findById(binDTO.getAreaId())
                    .orElseThrow(() -> new RuntimeException("Area not found with id: " + binDTO.getAreaId()));
            existingBin.setArea(newArea);
        }

        Bin updatedBin = binRepository.save(existingBin);
        return convertToDTO(updatedBin);
    }

    public BinDTO updateBinLevel(Long id, Integer level) {
        Bin bin = binRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bin not found with id: " + id));

        bin.setLevel(level);

        Bin updatedBin = binRepository.save(bin);
        return convertToDTO(updatedBin);
    }

    public BinDTO markBinAsEmptied(Long id) {
        Bin bin = binRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bin not found with id: " + id));

        bin.setLevel(0);
        bin.setLastEmptied(LocalDateTime.now());

        bin.setNextScheduledEmpty(LocalDateTime.now().plusDays(10));

        Bin updatedBin = binRepository.save(bin);
        return convertToDTO(updatedBin);
    }

    public void deleteBin(Long id) {
        if (!binRepository.existsById(id)) {
            throw new RuntimeException("Bin not found with id: " + id);
        }
        binRepository.deleteById(id);
    }

    public StatsDTO getStats() {
        long totalBins = binRepository.count();
        long fullBins = binRepository.countByStatus(BinStatus.FULL);
        long nearFullBins = binRepository.countByStatus(BinStatus.WARNING);
        long offlineBins = binRepository.countByStatus(BinStatus.OFFLINE);
        long activeBins = binRepository.countByStatus(BinStatus.ACTIVE);
        Double averageLevel = binRepository.getAverageLevel();

        return new StatsDTO(totalBins, fullBins, nearFullBins, offlineBins, activeBins,
                averageLevel != null ? averageLevel : 0.0);
    }

    // Conversion methods
    private BinDTO convertToDTO(Bin bin) {
        BinDTO dto = new BinDTO();
        dto.setId(bin.getId());
        dto.setCode(bin.getCode());
        dto.setName(bin.getName());
        dto.setLevel(bin.getLevel());
        dto.setStatus(bin.getStatus());
        dto.setOnline(bin.getOnline());

        dto.setLatitude(bin.getLatitude());
        dto.setLongitude(bin.getLongitude());
        dto.setLastEmptied(bin.getLastEmptied());
        dto.setNextScheduledEmpty(bin.getNextScheduledEmpty());
        dto.setUpdatedAt(bin.getUpdatedAt());

        if (bin.getArea() != null) {
            dto.setAreaId(bin.getArea().getId());
            dto.setAreaName(bin.getArea().getName());
        }

        return dto;
    }

    private Bin convertToEntity(BinDTO dto) {
        Bin bin = new Bin();
        bin.setCode(dto.getCode());
        bin.setName(dto.getName());
        bin.setLevel(dto.getLevel() != null ? dto.getLevel() : 0);
        bin.setOnline(dto.getOnline() != null ? dto.getOnline() : true);

        bin.setLatitude(dto.getLatitude());
        bin.setLongitude(dto.getLongitude());

        return bin;
    }
}
