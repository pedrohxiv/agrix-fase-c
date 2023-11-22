package com.betrybe.agrix.controllers;

import com.betrybe.agrix.controllers.dto.CropDto;
import com.betrybe.agrix.controllers.exceptions.NotFoundException;
import com.betrybe.agrix.models.entities.Crop;
import com.betrybe.agrix.models.entities.Fertilizer;
import com.betrybe.agrix.services.CropService;
import com.betrybe.agrix.services.FertilizerService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Crop controller class. */
@RestController
@RequestMapping(value = "/crops")
public class CropController {
  /** Attributes. */
  private final CropService cropService;
  private final FertilizerService fertilizerService;

  /** Constructor method. */
  public CropController(CropService cropService, FertilizerService fertilizerService) {
    this.cropService = cropService;
    this.fertilizerService = fertilizerService;
  }

  /** GET crops method. */
  @GetMapping()
  public List<CropDto> getAllCrops() {
    List<Crop> allCrops = cropService.getAllCrops();

    return allCrops.stream()
        .map(crop -> new CropDto(
            crop.getId(),
            crop.getName(),
            crop.getPlantedArea(),
            crop.getPlantedDate(),
            crop.getHarvestDate(),
            crop.getFarm().getId()))
        .collect(Collectors.toList());
  }

  /** GET crop method. */
  @GetMapping(value = "/{cropId}")
  public ResponseEntity<CropDto> getCropById(@PathVariable Long cropId) {
    Optional<Crop> optionalCrop = cropService.getCropById(cropId);

    if (optionalCrop.isEmpty()) {
      throw new NotFoundException("Plantação não encontrada!");
    }

    Crop crop = optionalCrop.get();

    return ResponseEntity.status(HttpStatus.OK).body(new CropDto(
        crop.getId(),
        crop.getName(),
        crop.getPlantedArea(),
        crop.getPlantedDate(),
        crop.getHarvestDate(),
        crop.getFarm().getId()));
  }

  /** GET crops in interval method. */
  @GetMapping(value = "/search")
  public List<CropDto> getCropsInInterval(
      @RequestParam LocalDate start,
      @RequestParam LocalDate end) {
    List<Crop> cropsInInterval = cropService.getCropsInInterval(start, end);

    return cropsInInterval.stream()
        .map(crop -> new CropDto(
            crop.getId(),
            crop.getName(),
            crop.getPlantedArea(),
            crop.getPlantedDate(),
            crop.getHarvestDate(),
            crop.getFarm().getId()))
        .collect(Collectors.toList());
  }

  /** POST associate crop and fertilizer method. */
  @PostMapping(value = "/{cropId}/fertilizers/{fertilizerId}")
  public ResponseEntity<String> associateCropAndFertilizer(
      @PathVariable Long cropId,
      @PathVariable Long fertilizerId) {
    Optional<Crop> optionalCrop = cropService.getCropById(cropId);

    if (optionalCrop.isEmpty()) {
      throw new NotFoundException("Plantação não encontrada!");
    }

    Optional<Fertilizer> optionalFertilizer = fertilizerService.getFertilizerById(fertilizerId);

    if (optionalFertilizer.isEmpty()) {
      throw new NotFoundException("Fertilizante não encontrado!");
    }

    Crop crop = optionalCrop.get();
    Fertilizer fertilizer = optionalFertilizer.get();

    cropService.associateCropAndFertilizer(crop.getId(), fertilizer.getId());

    return ResponseEntity.status(HttpStatus.CREATED).body(
        "Fertilizante e plantação associados com sucesso!");
  }

  /** GET fertilizers associated to crop method. */
  @GetMapping(value = "{cropId}/fertilizers")
  public List<Fertilizer> getFertilizers(@PathVariable Long cropId) {
    Optional<Crop> optionalCrop = cropService.getCropById(cropId);

    if (optionalCrop.isEmpty()) {
      throw new NotFoundException("Plantação não encontrada!");
    }

    Crop crop = optionalCrop.get();

    return crop.getFertilizers();
  }

  /** Exception handler. */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }
}
