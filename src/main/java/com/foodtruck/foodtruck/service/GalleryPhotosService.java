package com.foodtruck.foodtruck.service;

import com.foodtruck.foodtruck.entity.GalleryPhotos;

public interface GalleryPhotosService {
    public GalleryPhotos addMenu(GalleryPhotos menuEntity);

    public Boolean deleteMenu(Long id);

}
