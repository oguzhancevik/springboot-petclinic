package io.github.oguzhancevik.springbootpetclinic.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "petclinic")
public class PetClinicProperties {

    private boolean displayOwnersWithPets;

    public boolean isDisplayOwnersWithPets() {
        return displayOwnersWithPets;
    }

    public void setDisplayOwnersWithPets(boolean displayOwnersWithPets) {
        this.displayOwnersWithPets = displayOwnersWithPets;
    }
}
