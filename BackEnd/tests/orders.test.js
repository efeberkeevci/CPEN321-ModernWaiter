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

      it('Creates a new order with invalid user id', async done => {
        // Arrange
        const url = `/orders`
        const req = 
          {
            "userId" : 100,
            "tableId" : 1,
            "restaurantId" : 1,
            "amount" : 0,
            "hasPaid" : 0,
            "isActive" : 0
          }

        // Act
        const response = await request.post(url).send(req)

        // Assert
        expect(response.status).toBe(400)
        done()
      })

      it('Creates a new order with invalid table id', async done => {
        // Arrange
        const url = `/orders`
        const req = 
          {
            "userId" : 1,
            "tableId" : 100,
            "restaurantId" : 1,
            "amount" : 0,
            "hasPaid" : 0,
            "isActive" : 0
          }

        // Act
        const response = await request.post(url).send(req)

        // Assert
        expect(response.status).toBe(400)
        done()
      })

      it('Creates a new order with invalid restaurant id', async done => {
        // Arrange
        const url = `/orders`
        const req = 
          {
            "userId" : 1,
            "tableId" : 1,
            "restaurantId" : 100,
            "amount" : 0,
            "hasPaid" : 0,
            "isActive" : 0
          }

        // Act
        const response = await request.post(url).send(req)

        // Assert
        expect(response.status).toBe(400)
        done()
      })

      it('Creates a new order with missing user id', async done => {
        // Arrange
        const url = `/orders`
        const req = 
          {
            "tableId" : 1,
            "restaurantId" : 1,
            "amount" : 0,
            "hasPaid" : 0,
            "isActive" : 0
          }

        // Act
        const response = await request.post(url).send(req)

        // Assert
        expect(response.status).toBe(400)
        done()
      })

      it('Creates a new order with missing table id', async done => {
        // Arrange
        const url = `/orders`
        const req = 
          {
            "userId" : 1,
            "restaurantId" : 1,
            "amount" : 0,
            "hasPaid" : 0,
            "isActive" : 0
          }

        // Act
        const response = await request.post(url).send(req)

        // Assert
        expect(response.status).toBe(400)
        done()
      })

      it('Creates a new order with missing restaurant id', async done => {
        // Arrange
        const url = `/orders`
        const req = 
          {
            "tableId" : 1,
            "userId" : 1,
            "amount" : 0,
            "hasPaid" : 0,
            "isActive" : 0
          }

        // Act
        const response = await request.post(url).send(req)

        // Assert
        expect(response.status).toBe(400)
        done()
      })

      it('Creates a new order with missing amount', async done => {
        // Arrange
        const url = `/orders`
        const req = 
          {
            "userId" : 1,
            "tableId" : 1,
            "restaurantId" : 100,
            "hasPaid" : 0,
            "isActive" : 0
          }

        // Act
        const response = await request.post(url).send(req)

        // Assert
        expect(response.status).toBe(400)
        done()
      })

      it('Creates a new order with missing hasPaid', async done => {
        // Arrange
        const url = `/orders`
        const req = 
          {
            "userId" : 1,
            "tableId" : 1,
            "restaurantId" : 100,
            "amount" : 0,
            "isActive" : 0
          }

        // Act
        const response = await request.post(url).send(req)

        // Assert
        expect(response.status).toBe(400)
        done()
      })

      it('Creates a new order with missing isActive', async done => {
        // Arrange
        const url = `/orders`
        const req = 
          {
            "userId" : 1,
            "tableId" : 1,
            "restaurantId" : 100,
            "amount" : 0,
            "hasPaid" : 0
          }

        // Act
        const response = await request.post(url).send(req)

        // Assert
        expect(response.status).toBe(400)
        done()
      })
  })

  
