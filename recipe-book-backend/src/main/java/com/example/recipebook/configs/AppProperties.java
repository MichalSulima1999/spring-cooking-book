package com.example.recipebook.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Slf4j
public class AppProperties {
    private final Directories directories = new Directories();

    public Directories getDirectories() {
        return directories;
    }

    public static final class Directories {
        private String recipe;

        public String getRecipe() {
            log.info(recipe);
            return "recipe-images/";
        }

        public Directories recipe(String recipe) {
            this.recipe = recipe;
            return this;
        }
    }
}
