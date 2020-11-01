const request = require('supertest')
const app = require('../backend_server')

describe("GET /items/:id ", () => {
    test("It should respond with an array of items", async () => {
      const response = await request(app).get("/items/1");
      expect(response.body).anything();
      expect(response.statusCode).toBe(200);
    });
  });