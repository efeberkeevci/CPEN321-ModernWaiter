const app = require('../backend_server')
const supertest = require('supertest')
const request = supertest(app)

describe('Test createOrder()', () => {
    it('Creates a new order', async done => {
        // Arrange
        const url = `/orders`
        const req = 
          {
            "userId" : 1,
            "tableId" : 1,
            "restaurantId" : 1,
            "amount" : 0,
            "hasPaid" : 0,
            "isActive" : 0
          }

        // Act
        const response = await request.post(url).send(req)

        // Assert
        expect(response.status).toBe(201)
        done()
      })
  })

  
