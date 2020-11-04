const app = require('../backend_server')
const supertest = require('supertest')
const request = supertest(app)

describe('Test getMenu()', () => {
    it('Gets the menu for a restaurant', async done => {
        // Arrange
        const restaurantId = 1
        const url = `/items/${restaurantId}`
        const expectedFirstItem =
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
          }

        // Act
        const response = await request.get(url)

        // Assert
        expect(response.status).toBe(200)
        expect(response.body[0]).toStrictEqual(expectedFirstItem)
        done()
      })

      it('Gets an empty list for non-existent restaurant', async done => {
        // Arrange
        const restaurantId = 100
        const url = `/items/${restaurantId}`
        const expectedMenu = []

        // Act
        const response = await request.get(url)

        // Assert
        expect(response.status).toBe(200)
        expect(response.body).toStrictEqual(expectedMenu)
        done()
      })
  })

  
