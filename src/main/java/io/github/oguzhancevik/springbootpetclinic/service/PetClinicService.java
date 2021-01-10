package io.github.oguzhancevik.springbootpetclinic.service;

import io.github.oguzhancevik.springbootpetclinic.exception.OwnerNotFoundException;
import io.github.oguzhancevik.springbootpetclinic.model.Owner;

import java.util.List;

public interface PetClinicService {

    List<Owner> findOwners();

    List<Owner> findOwners(String lastName);

    Owner findOwner(Long id) throws OwnerNotFoundException;

    Owner createOwner(Owner owner);

    Owner updateOwner(Owner owner);

    void deleteOwner(Long id);

}
