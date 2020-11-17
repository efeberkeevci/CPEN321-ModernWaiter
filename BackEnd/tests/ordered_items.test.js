const app = require('../backend_server')
const supertest = require('supertest')
const request = supertest(app)

//TODO: Check if written down is the right way to
//Tests for GET /ordered-items/{{orderId}}
describe("Test getOrderedItems()", ()=>{

    test("Testing Invalid orderId type", async done=>{

        //Arrange
        let param = "Invalid orderId of wrong data type"
        let path = "/ordered-items/"
        
        //Act
        const res = await request.get(path+param)

        //Assert
        expect(res.statusCode).toEqual(400)
        done()
    })
    
    test("Testing valid orderId type", async done=>{

        //Arrange
        let param = "1"
        let path = "/ordered-items/"
        
        //Act
        const res = await request.get(path+param)

        //Assert
        //TODO: What to do for the case query returns 400 although valid
        expect(res.statusCode).toEqual(200)
        expect(typeof res.body[0].id).toBe('number')
        expect(typeof res.body[0].orders_id).toBe('number')
        expect(typeof res.body[0].items_id).toBe('number')
        expect(typeof res.body[0].has_paid).toBe('number')
        expect(typeof res.body[0].is_selected).toBe('number')
        expect(typeof res.body[0].users_id).toBe('number')
        done()
    })

})

describe("Test addOrderedItems()",()=>{
    test("Test Invalid request body", async done=>{

        //Arrange
        let req_body =
                [
                    {"orderId" : "invalid orderId type",
                     "itemsId"  : "1"
                    },
                    {"orderId" : "2",
                     "itemsId"  : "invalid type"
                    }
                ]
        
        let path = "/ordered-items/"

        //Act
        const res = await request.post(path).send(req_body)

        //Assert
        expect(res.statusCode).toEqual(400)
        done()
    })

    test("Test Valid request body", async done=>{

        //Arrange
        let req_body =[
            {
                "orderId"  : 1,
                "itemsId"  : 1
            },
            {
                "orderId"  : 2,
                "itemsId"  : 1
            },
        ]
         
        let path = "/ordered-items/"

        //Act
        const res = await request.post(path).send(req_body)

        //Assert
        expect(res.statusCode).toEqual(201)
        done()  
    })
})

describe("Test updateSelectedStatus()",() =>{
    test("Test Invalid request body", async done=>{

        //Arrange
        let req_body =
                [
                    {"orderId" : "invalid orderId type",
                     "itemsId"  : "1",
                     "userId"  : 1
                    }
                ]
        
        let path = "/ordered-items/selected"

        //Act
        const res = await request.put(path).send(req_body)

        //Assert
        expect(res.statusCode).toEqual(400)
        done()
    })

    test("Test Valid request body", async done=>{

        //Arrange
        let req_body =[
            {
                "orderId" : 1,
                "itemsId"  : 1,
                "userId"  : 1
            }
        ]
         
        let path = "/ordered-items/selected"

        //Act
        const res = await request.put(path).send(req_body)

        //Assert
        expect(res.statusCode).toEqual(201)
        done()  
    })
})

describe("Test updateOrderedItemPaidStatus()", () =>{
    test("Test Invalid request body", async done=>{

        //Arrange
        let req_body =
                [
                    {"orderId" : "invalid orderId type",
                     "itemsId"  : "1"
                    },
                    {"orderId" : "2",
                     "itemsId"  : "invalid type"
                    }
                ]
        
        let path = "/ordered-items/paid"

        //Act
        const res = await request.put(path).send(req_body)

        //Assert
        expect(res.statusCode).toEqual(400)
        done()
    })

    test("Test Valid request body", async done=>{

        //Arrange
        let req_body =[
            {
                "orderId"  : 1,
                "itemsId"  : 1
            },
            {
                "orderId"  : 2,
                "itemsId"  : 1
            },
        ]
         
        let path = "/ordered-items/paid"

        //Act
        const res = await request.put(path).send(req_body)

        //Assert
        expect(res.statusCode).toEqual(201)
        done()  
    })
})
