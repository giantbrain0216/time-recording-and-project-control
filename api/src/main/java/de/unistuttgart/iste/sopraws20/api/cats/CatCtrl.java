package de.unistuttgart.iste.sopraws20.api.cats;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class CatCtrl {

    private AtomicLong counter;
    private List<Cat> cats;

    // executed after start-up and dependency injection
    @PostConstruct
    public void init() {
        this.counter = new AtomicLong();
        this.cats = new ArrayList<>();

        Cat octocat = new Cat(counter.getAndIncrement(), "Octocat", 42,
                "https://avatars1.githubusercontent.com/u/583231");
        this.cats.add(octocat);

        Cat grumpyCat = new Cat(counter.getAndIncrement(), "Grumpy Cat", 10,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dc/Grumpy_Cat_%2814556024763%29_%28cropped%29.jpg/220px-Grumpy_Cat_%2814556024763%29_%28cropped%29.jpg");
        this.cats.add(grumpyCat);
    }

    // get all cats
    @GetMapping("/cats")
    public List<Cat> getCats() {
        return this.cats;
    }

    // get a single cat
    @GetMapping("/cats/{id}")
    public Cat getCat(@PathVariable("id") long id) {

        for (Cat cat : this.cats) {
            if (cat.getId() == id) {
                return cat;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Cat with ID %s not found!", id));
    }

    // create a cat
    @PostMapping("/cats")
    @ResponseStatus(HttpStatus.CREATED)
    public Cat createCat(@Valid @RequestBody Cat requestBody) {
        Cat cat = new Cat(counter.getAndIncrement(), requestBody.getName(),
                requestBody.getAgeInYears(), requestBody.getPicUrl());
        this.cats.add(cat);
        return cat;
    }

    // update a cat
    @PutMapping("/cats/{id}")
    public Cat updateCat(@PathVariable("id") long id, @Valid @RequestBody Cat requestBody) {
        requestBody.setId(id);

        for (int x = 0; x < this.cats.size(); x++) {
            if (this.cats.get(x).getId() == id) {
                this.cats.set(x, requestBody);
                return requestBody;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Cat with ID %s not found!", id));
    }

    // delete a cat
    @DeleteMapping("/cats/{id}")
    public Cat deleteCat(@PathVariable("id") long id) {

        for (int x = 0; x < this.cats.size(); x++) {
            if (this.cats.get(x).getId() == id) {
                Cat deletedCat = this.cats.get(x);
                this.cats.remove(x);
                return deletedCat;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Cat with ID %s not found!", id));
    }
}
