package com.betrybe.agrix.services;

import com.betrybe.agrix.models.entities.Crop;
import com.betrybe.agrix.models.entities.Fertilizer;
import com.betrybe.agrix.models.repositories.CropRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Crop service class. */
@Service
public class CropService {
  /** Attributes. */
  private final CropRepository cropRepository;
  private final FertilizerService fertilizerService;

  /** Constructor method. */
  @Autowired
  public CropService(CropRepository cropRepository, FertilizerService fertilizerService) {
    this.cropRepository = cropRepository;
    this.fertilizerService = fertilizerService;
  }

  /** Get all crops method. */
  public List<Crop> getAllCrops() {
    return cropRepository.findAll();
  }

  /** Get crop by ID method. */
  public Optional<Crop> getCropById(Long cropId) {
    return cropRepository.findById(cropId);
  }

  /** Get crops in interval method. */
  public List<Crop> getCropsInInterval(LocalDate start, LocalDate end) {
    return cropRepository.findByHarvestDateBetween(start, end);
  }

  /** Associate crop and fertilizer method. */
  public Optional<Crop> associateCropAndFertilizer(Long cropId, Long fertilizerId) {
    Optional<Crop> optionalCrop = getCropById(cropId);

    if (optionalCrop.isEmpty()) {
      return Optional.empty();
    }

    Optional<Fertilizer> optionalFertilizer = fertilizerService.getFertilizerById(fertilizerId);

    if (optionalFertilizer.isEmpty()) {
      return Optional.empty();
    }

    Crop crop = optionalCrop.get();
    Fertilizer fertilizer = optionalFertilizer.get();

    crop.getFertilizers().add(fertilizer);

    return Optional.of(cropRepository.save(crop));
  }
}
