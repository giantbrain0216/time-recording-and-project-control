# SoPra WS20/21: Skeleton Project

> This repository contains example projects for a RESTful API with Spring Boot (`api/`) and for a single page application with Vue.js (`frontend/`).
> Please refer to the `README.md` files in the respective folders for concrete instructions.

## Quickstart

Assuming all prerequisites are fulfilled, you can follow these instructions to get to know the application:
- Start the API by navigating into its folder and executing `./mvnw spring-boot:run`.
- Open http://localhost:8080/api/v1/cats in your browser. It will show all available cats in the system.
- Open http://localhost:8080/api/v1/cats/1 in your browser. It will show the cat with ID `1`.
- Open http://localhost:8080/api/v1/cats/42 in your browser. It will show a `404 Not found` error because there is no cat with ID `42` in the system.
- Now start the frontend by navigating into its folder and executing `npm install` and then `npm run serve`.
- It will host the UI at http://localhost:8000, which you can now also open in your browser.
- Navigate to the cats view by clicking on the respective link or via the direct ULR http://localhost:8000/#/cats.
- Delete a cat. Then confirm its deletion via the API by opening http://localhost:8080/api/v1/cats in your browser again.
