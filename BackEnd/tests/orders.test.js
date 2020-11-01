const orders = require('../routes/orders.js')

describe('checkOrder', () => {
  test('should 200 with username from session if session data is set', async () => {
    const req = {body: { userId : "1", tableId : "1", restaurantId : "1", amount : "55.43", hasPaid : "0", isActive : "1"}}
    const res = mockResponse();
    orders.createOrder(req, res);
    expect(res.status).toHaveBeenCalledWith(200);
    expect(res.json).toEqual("");
  });
});

const mockResponse = () => {
  const res = {};
  res.status = jest.fn().mockReturnValue(res);
  res.json = jest.fn().mockReturnValue(res);
  return res;
};
