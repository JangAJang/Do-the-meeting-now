package com.swprogramming.dothemeetingnow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@SpringBootApplication
public class DoTheMeetingNowApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(DoTheMeetingNowApplication.class, args);
    }
}
