package com.example.service.impl;

import com.example.builder.AppBuilder;
import com.example.exception.AppNotFoundException;
import com.example.model.domain.App;
import com.example.model.dto.AppDTO;
import com.example.repository.AppRepository;
import com.example.service.AppService;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class AppServiceImpl implements AppService {

    private final AppRepository repository;
    private final AppBuilder builder;

    @Override
    public Long createNewApp(AppDTO dto) {
        return Stream.of(dto)
            .map(builder::build)
            .map(repository::save)
            .map(App::getId)
            .findFirst()
            .get();
    }

    @Override
    public List<Optional<AppDTO>> getAllApps() {
        return repository.findAll()
            .stream()
            .map(builder::build)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<AppDTO> getAppById(Long id) {
        return repository.findById(id)
            .map(builder::build)
            .orElseThrow(() -> new AppNotFoundException(String.format("No such App for id '%s'", id)));
    }

    @Transactional
    @Override
    public Optional<AppDTO> updateApp(Long id, AppDTO dto) {
        return repository.findById(id)
            .map(model -> builder.build(dto, model))
            .map(repository::save)
            .map(builder::build)
            .orElseThrow(() -> new AppNotFoundException(String.format("No such App for id '%s'", id)));
    }

    @Override
    public void deleteAppById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void populate() {
        val faker = new Faker();
        IntStream.range(0, 100).forEach(i -> {
            App app = App.builder()
                .author(faker.app().author())
                .name(faker.app().name())
                .version(faker.app().version())
                .build();

            repository.save(app);
        });
    }
}
