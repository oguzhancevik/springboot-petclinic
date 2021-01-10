package io.github.oguzhancevik.springbootpetclinic.repository;

import io.github.oguzhancevik.springbootpetclinic.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByOwnerId(Long ownerId);

    void deleteByOwnerId(Long ownerId);
}
