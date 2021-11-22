package com.phonebook.spring;

import com.phonebook.main.InMemoryRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Keeps phoneBook data in memory in ordered in accordance to addition.
 */
@Repository
public class InMemoryRepositoryIml implements InMemoryRepository {

    private Map<String, Set<String>> data;

    /**
     * no args constructor
     */
    public InMemoryRepositoryIml() {
        // LinkedHashMap is chosen because usually iteration order matters
        this(new LinkedHashMap<>());
    }

    /**
     * this constructor allows to inject initial data to the repository
     *
     * @param data
     */
    public InMemoryRepositoryIml(Map<String, Set<String>> data) {
        this.data = new LinkedHashMap<>(data);
    }

    @Override
    public Map<String, Set<String>> findAll() {
        return new LinkedHashMap<>(this.data);
    }

    @Override
    public Set<String> findAllPhonesByName(String name) {
        return data.get(name);
    }

    @Override
    public String findNameByPhone(String phone) {
        for (Map.Entry<String, Set<String>> entry : data.entrySet()) {
            for (String phoneEntry : entry.getValue()) {
                if (phoneEntry.equals(phone)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    @Override
    public void addPhone(String name, String phone) {
        Set phonesSet = findAllPhonesByName(name);
        String[] phones = phone.split(",");
        for (String iter : phones) {
            phonesSet.add(iter);
        }
        data.put(name, phonesSet);
    }

    @Override
    public void removePhone(String phone) throws IllegalArgumentException {
        String name = findNameByPhone(phone);
        if (name != null) {
            if (data.get(name).size() > 1) {
                Set<String> updatedPhones = findAllPhonesByName(name);
                updatedPhones.remove(phone);
                data.put(name, updatedPhones);
            } else {
                data.remove(name);
            }
        } else {
            throw new IllegalArgumentException("Incorrect phone was provided!");
        }
    }
}
