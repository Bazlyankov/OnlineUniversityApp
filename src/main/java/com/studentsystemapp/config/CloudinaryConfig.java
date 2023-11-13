package com.studentsystemapp.config;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {



    @Bean
    public Cloudinary cloudinary() {

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dm3g00ezq",
                "api_key", "439822333791418",
                "api_secret", "DFVUzwEUc2WJ9M0hhW9Ei7HtAWo",
                "resource_type", "auto"));

        return cloudinary;
    }

}
