package com.bharat.hystrix.example

import com.google.common.util.concurrent.RateLimiter
import org.mockserver.model.{HttpRequest, HttpResponse}

/**
 * @author bharat
 */
object AppStart extends App {

    val y = new MockServerImpl()

    def generateTraffic(totalNoOfReq : Int, noOfRequestPerSec:Int):Unit={
        val rateLimiter = RateLimiter.create(noOfRequestPerSec)
        val req1 = new HttpRequest().withPath("/api").withQueryStringParameter("simulate", "failure")
        y.createExpectation(req1)
        for(i<- List.range(0,totalNoOfReq) ){
            rateLimiter.acquire()
            new HystrixBackedCall("failure").execute()
          }
    }

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
        try{
        println("\n\n\n\n\n\nRESPONSE FROM FAILURE MOCK-SERVER : ----> " + x.execute())
        }catch{
            case e  => println( "RESPONSE FROM FAILURE MOCK-SERVER"  + e);
        }

        println("GRRRRH")
    }
    successCall()
    failCall()
    generateTraffic(1000,30)
    y.stopMockServer
}