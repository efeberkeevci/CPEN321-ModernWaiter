const request = require('supertest')
const app = require('../backend_server')
const test_menu = [
    {
        "id": 1,
        "restaurant_id": 1,
        "name": "Spicy Ahi Roll",
        "type": "Sushi",
        "cost": 16.5,
        "description": "ocean wise ahi tuna, mango, avocado, asparagus, cucumber, sesame soy paper, wasabi mayo, cripy yam curls",
        "calories": 500,
        "popularity_count": 3,
        "image": "gs://modern-waiter-47e96.appspot.com/dummy-spicy-ahi.jpg"
    },
    {
        "id": 2,
        "restaurant_id": 1,
        "name": "Prawn Crunch Roll",
        "type": "Sushi",
        "cost": 16,
        "description": "crispy prawn, mango, avocado, asparagus, cucumber, sesame soy paper, sriracha mayo, soy glaze",
        "calories": 500,
        "popularity_count": 4,
        "image": "gs://modern-waiter-47e96.appspot.com/dummy-prawn-crunch.jpg"
    },
    {
        "id": 3,
        "restaurant_id": 1,
        "name": "Ceviche",
        "type": "Appetizers",
        "cost": 18.5,
        "description": "ocean wise lois lake steelhead, sustainably harvested prawns, avocado, chili, thai basil, mint, peruvian leche de tigre marinade",
        "calories": 750,
        "popularity_count": 2,
        "image": "gs://modern-waiter-47e96.appspot.com/dummy-prawn-crunch.jpg"
    },
    {
        "id": 4,
        "restaurant_id": 1,
        "name": "Mini crispy chicken sandwiches",
        "type": "Appetizers",
        "cost": 16,
        "description": "spicy panko-crusted chicken, swiss cheese, sambal mayo, lettuce, tomato, pickle, onion",
        "calories": 1100,
        "popularity_count": 5,
        "image": "gs://modern-waiter-47e96.appspot.com/dummy-mini-crispy.jpg"
    },
    {
        "id": 5,
        "restaurant_id": 1,
        "name": "Modern bowl",
        "type": "Bowls",
        "cost": 21.5,
        "description": "tabbouleh, pineapple salsa, broccoli, tomatoes, cucumber, fresh greens, jasmine rice, miso carrot ginger sauce, chicken",
        "calories": 450,
        "popularity_count": 3,
        "image": "gs://modern-waiter-47e96.appspot.com/dummy-modern-bowl.jpg"
    },
    {
        "id": 6,
        "restaurant_id": 1,
        "name": "Tuna poke bowl",
        "type": "Bowls",
        "cost": 20.5,
        "description": "sesame ginger ocean wise, ahi, jasmine rice, mango, cucumber, mango, cucumber, avocado, edamane, radish, crispy, tempura",
        "calories": 800,
        "popularity_count": 4,
        "image": "gs://modern-waiter-47e96.appspot.com/dummy-tuna-bowl.jpg"
    }
]

describe("GET /items/{{id}}", () => {
    test("It should respond with an array of items", async () => {
      const response = await request(app).get("/items/1");
      expect(response.body).toEqual(test_menu);
      expect(response.statusCode).toBe(200);
    });
  });
