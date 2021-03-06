package com.bharat.hystrix.example

import java.util.concurrent.TimeUnit

import com.netflix.hystrix.HystrixCommand.Setter
import com.netflix.hystrix.{HystrixCommandProperties, HystrixCommand, HystrixCommandGroupKey}
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients

/**
 * @author bharat
 */
class HystrixBackedCall(query :String) extends
                  HystrixCommand[String](
                    Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("myMockServer")).
                      andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(1000).
                        withExecutionTimeoutEnabled(true))){

  override def run():String={
    val httpClient = HttpClients.custom().setConnectionTimeToLive(1000,TimeUnit.MILLISECONDS).build()
    val httpGet = createRequest(query)
    val response =  httpClient.execute(httpGet)
    try{
      IOUtils.toString(response.getEntity.getContent)
    }finally {
      response.close()
      httpClient.close()
    }
  }

  def createRequest(queryString:String):HttpGet=new HttpGet("http://localhost:19898/api?simulate=" + queryString)

  override def getFallback():String ={
      throw new Exception("circuit is opened")
  }
  
}