package com.betrybe.agrix.services;

import com.betrybe.agrix.models.entities.Crop;
import com.betrybe.agrix.models.entities.Farm;
import com.betrybe.agrix.models.repositories.CropRepository;
import com.betrybe.agrix.models.repositories.FarmRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Farm service class. */
@Service
public class FarmService {
  /** Attributes. */
  private final FarmRepository farmRepository;
  private final CropRepository cropRepository;

  /** Constructor method. */
  @Autowired
  public FarmService(FarmRepository farmRepository, CropRepository cropRepository) {
    this.farmRepository = farmRepository;
    this.cropRepository = cropRepository;
  }

  /** Create farm method. */
  public Farm createFarm(Farm farm) {
    return farmRepository.save(farm);
  }

  /** Get all farms method. */
  public List<Farm> getAllFarms() {
    return farmRepository.findAll();
  }

  /** Get farm by ID method. */
  public Optional<Farm> getFarmById(Long farmId) {
    return farmRepository.findById(farmId);
  }

  /** Create crop method. */
  public Optional<Crop> createCrop(Long farmId, Crop crop) {
    Optional<Farm> optionalFarm = farmRepository.findById(farmId);

    if (optionalFarm.isEmpty()) {
      return Optional.empty();
    }

    Farm farm = optionalFarm.get();
    crop.setFarm(farm);
    Crop newCrop = cropRepository.save(crop);
    farm.getCrops().add(newCrop);

    return Optional.of(newCrop);
  }

  /** Get all crops by farm ID method. */
  public Optional<List<Crop>> getAllCropsByFarmId(Long farmId) {
    Optional<Farm> optionalFarm = farmRepository.findById(farmId);

    if (optionalFarm.isEmpty()) {
      return Optional.empty();
    }

    Farm farm = optionalFarm.get();

    return Optional.of(farm.getCrops());
  }
}
