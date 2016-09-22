package com.bharat.hystrix.example

import java.util.concurrent.TimeUnit

import org.mockserver.integration.ClientAndServer
import org.mockserver.mock.action.ExpectationCallback
import org.mockserver.model.{Delay, HttpCallback, HttpResponse, HttpRequest}

/**
  * Created by bharat on 17/7/16.
  */
class MockServerImpl  {
  val clientAndServer = new ClientAndServer(1080)

  def stopMockServer() = clientAndServer.stop()

  def createExpectation(request:HttpRequest):Unit ={
    clientAndServer.when(request).callback(new HttpCallback().withCallbackClass("com.bharat.hystrix.example.MockServerImpl"))
  }

}

class MyMockCallback extends ExpectationCallback{
  override def handle(httpRequest: HttpRequest): HttpResponse = {
    val x = httpRequest.getQueryStringParameters.get(0)
    if("success".equalsIgnoreCase(x.getValues.get(0).getValue)){
      new HttpResponse().withBody("hello")
    }else{
      new HttpResponse().withDelay(new Delay(TimeUnit.MILLISECONDS,20000)).withBody("hello").applyDelay()
    }
  }
}
