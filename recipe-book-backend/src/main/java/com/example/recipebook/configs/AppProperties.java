package com.example.recipebook.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Directories directories = new Directories();

    public Directories getFileDirectories() {
        return directories;
    }

    public static final class Directories {
        private String recipeImageDirectory;

        public String getRecipeImageDirectory() {
            return recipeImageDirectory;
        }

        public Directories recipeImageDirectory(String recipeImageDirectory) {
            this.recipeImageDirectory = recipeImageDirectory;
            return this;
        }
    }
}
