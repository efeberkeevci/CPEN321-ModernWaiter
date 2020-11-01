const request = require('supertest')
const app = require('../backend_server')

jest.mock('../mocks/items')

describe("GET /items/{{restaurantId}}", () => {
    test("It should respond with an array of items", () => {
      const response = await request(app).get("/items/1");
      expect(response.body).hasAssertions();
      expect(response.statusCode).toBe(200);
      done()
    });
  });
