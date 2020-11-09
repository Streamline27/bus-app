# Home task for an IT company

#### How to run

1. Run **docker-compose up** in order to start PostgreSQL container.
2. Run application with default profile.
3. Run tests as it is done usually. (Tests require docker to be installed)

#### Try this our:
* Swagger-UI is available at: http://localhost:8080/bus-app/swagger-ui.html
  * Try playing around with **/draft-price** endpoint. Pay attention how params are validated and how errors are accumulated in the JSON.
  * Use it to make sure that endpoints work as expected.
* Try running tests.
  * WIremock is used to showcase how 3rd party JSON service can be mocked.
  * Docker PostgreSQL test container is used to run database for integration tests. No additional setup for H2 is required.
  * The idea was to test application with integration tests only.
