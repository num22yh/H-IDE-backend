package org.example.backend.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public List<TestEntity> getAllEntities() {
        return testRepository.findAll();
    }

    public Optional<TestEntity> getEntityById(Long id) {
        return testRepository.findById(id);
    }

    public TestEntity createOrUpdateEntity(TestEntity entity) {
        return testRepository.save(entity);
    }

    public void deleteEntity(Long id) {
        testRepository.deleteById(id);
    }
}
