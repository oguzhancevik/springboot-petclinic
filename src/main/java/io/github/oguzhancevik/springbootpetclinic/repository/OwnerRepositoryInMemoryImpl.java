package io.github.oguzhancevik.springbootpetclinic.repository;

import io.github.oguzhancevik.springbootpetclinic.model.Owner;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class OwnerRepositoryInMemoryImpl implements OwnerRepository {

    private Map<Long, Owner> ownersMap = new HashMap<>();

    public OwnerRepositoryInMemoryImpl() {
        Owner owner1 = new Owner();
        owner1.setId(1L);
        owner1.setFirstName("John");
        owner1.setLastName("Doe");

        Owner owner2 = new Owner();
        owner2.setId(2L);
        owner2.setFirstName("Arturo");
        owner2.setLastName("Kim");

        Owner owner3 = new Owner();
        owner3.setId(3L);
        owner3.setFirstName("Lance");
        owner3.setLastName("Tyler");

        Owner owner4 = new Owner();
        owner4.setId(4L);
        owner4.setFirstName("Andres");
        owner4.setLastName("Marquez");

        ownersMap.put(owner1.getId(), owner1);
        ownersMap.put(owner2.getId(), owner2);
        ownersMap.put(owner3.getId(), owner3);
        ownersMap.put(owner4.getId(), owner4);
    }

    @Override
    public Owner findById(Long id) {
        return ownersMap.get(id);
    }

    @Override
    public void create(Owner owner) {
        owner.setId(new Date().getTime());
        ownersMap.put(owner.getId(), owner);
    }

    @Override
    public List<Owner> findByLastName(String lastName) {
        return ownersMap.values().stream()
                .filter(o -> o.getLastName().equals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public Owner update(Owner owner) {
        ownersMap.replace(owner.getId(), owner);
        return owner;
    }

    @Override
    public void delete(Long id) {
        ownersMap.remove(id);
    }

    @Override
    public List<Owner> findAll() {
        return new ArrayList<>(ownersMap.values());
    }

}