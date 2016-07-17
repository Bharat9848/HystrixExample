package com.bharat.hystrix.example

import java.util.concurrent.TimeUnit

import org.mockserver.mockserver.MockServer
import org.mockserver.model.{Delay, HttpResponse, HttpRequest}

/**
 * @author bharat
 */
object AppStart extends App {

    val y = new MockServerImpl()


    def successCall()={
        val x = new HystrixBackedCall("success")
        val req1 = new HttpRequest().withPath("/api").withQueryStringParameter("simulate", "success")
        val res1 = new HttpResponse().withBody("hello")
        y.createExpectation(req1)
        println("\n\n\n\n\n\nRESPONSE FROM  SUCCESS CALL MOCK-SERVER : ----> " + x.execute())
    }

    def failCall()={
        val x = new HystrixBackedCall("failure")
        val req1 = new HttpRequest().withPath("/api").withQueryStringParameter("simulate", "failure")
        y.createExpectation(req1)
        println("\n\n\n\n\n\nRESPONSE FROM FAILURE MOCK-SERVER : ----> " + x.execute())
    }

    successCall()
    failCall()
    y.stopMockServer
}