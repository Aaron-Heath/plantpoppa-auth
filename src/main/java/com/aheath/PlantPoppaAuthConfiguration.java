package com.aheath;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import jakarta.validation.Valid;

public class PlantPoppaAuthConfiguration extends Configuration {
    @Valid
    private DataSourceFactory database;


    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }
}
