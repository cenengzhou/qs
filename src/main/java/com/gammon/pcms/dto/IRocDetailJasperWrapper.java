package com.gammon.pcms.dto;

public interface IRocDetailJasperWrapper {

  // **
  // ** field must same order as nativeQuery column at RocRepository
  // ** 

  double getAmountBest();

  double getAmountBestMovement();

  double getAmountRealistic();

  double getAmountRealisticMovement();

  double getAmountWorst();
  
  double getAmountWorstMovement();

  String getCategory();

  String getDescription();

  String getImpact();

  String getItemNo();
  
  int getMonth();

  String getProjectRef();

  String getRemark();

  int getRocId();

  int getYear();

}
