package com.smartbin.service;

import com.smartbin.dto.AreaDTO;
import com.smartbin.entity.Area;
import com.smartbin.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    public List<AreaDTO> getAllAreas() {
        List<Area> areas = areaRepository.findAll();
        return areas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<AreaDTO> getAllAreasWithBinCount() {
        List<Area> areas = areaRepository.findAllWithBins();
        return areas.stream().map(area -> {
            AreaDTO dto = convertToDTO(area);
            dto.setBinCount(area.getBins() != null ? area.getBins().size() : 0);
            return dto;
        }).collect(Collectors.toList());
    }

    public Optional<AreaDTO> getAreaById(Long id) {
        return areaRepository.findById(id).map(this::convertToDTO);
    }

    public Optional<AreaDTO> getAreaByName(String name) {
        return areaRepository.findByName(name).map(this::convertToDTO);
    }

    public List<AreaDTO> searchAreasByName(String name) {
        List<Area> areas = areaRepository.findByNameContainingIgnoreCase(name);
        return areas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public AreaDTO createArea(AreaDTO areaDTO) {
        if (areaRepository.existsByName(areaDTO.getName())) {
            throw new RuntimeException("Area with name " + areaDTO.getName() + " already exists");
        }

        Area area = convertToEntity(areaDTO);
        Area savedArea = areaRepository.save(area);
        return convertToDTO(savedArea);
    }

    public AreaDTO updateArea(Long id, AreaDTO areaDTO) {
        Area existingArea = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!existingArea.getName().equals(areaDTO.getName()) &&
                areaRepository.existsByName(areaDTO.getName())) {
            throw new RuntimeException("Area with name " + areaDTO.getName() + " already exists");
        }

        existingArea.setName(areaDTO.getName());
        existingArea.setDescription(areaDTO.getDescription());

        Area updatedArea = areaRepository.save(existingArea);
        return convertToDTO(updatedArea);
    }

    public void deleteArea(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area not found with id: " + id));

        // Check if area has bins
        if (area.getBins() != null && !area.getBins().isEmpty()) {
            throw new RuntimeException("Cannot delete area that contains bins. Please move or delete bins first.");
        }

        areaRepository.deleteById(id);
    }

    // Conversion methods
    private AreaDTO convertToDTO(Area area) {
        AreaDTO dto = new AreaDTO();
        dto.setId(area.getId());
        dto.setName(area.getName());
        dto.setDescription(area.getDescription());
        return dto;
    }

    private Area convertToEntity(AreaDTO dto) {
        Area area = new Area();
        area.setName(dto.getName());
        area.setDescription(dto.getDescription());
        return area;
    }
}
