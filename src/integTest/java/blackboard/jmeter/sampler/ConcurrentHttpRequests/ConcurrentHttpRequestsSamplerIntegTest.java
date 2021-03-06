package blackboard.jmeter.sampler.ConcurrentHttpRequests;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.Before;
import org.junit.Test;

import blackboard.jmeter.sampler.ConcurrentHttpRequests.config.HttpRequestConfig;
import blackboard.jmeter.sampler.ConcurrentHttpRequests.config.MultipleHttpRequestsConfig;
import blackboard.jmeter.sampler.ConcurrentHttpRequests.gui.ListContentSplitPanel;

public class ConcurrentHttpRequestsSamplerIntegTest
{
  ConcurrentHttpRequestsSampler sampler;
  //2 valid urls, and 1 invalid
  String[] urls = new String[] { "http://www.google.com", "http://www.baidu.com", "http://www.abc.e.e.f.com" };

  @Before
  public void setUp()
  {
    JMeterUtils.setLocale(new Locale("ignoreResources"));
    sampler = new ConcurrentHttpRequestsSampler();

    MultipleHttpRequestsConfig wholeConfig = new MultipleHttpRequestsConfig();
    List<HttpRequestConfig> configs = new ArrayList<HttpRequestConfig>();
    for ( int i = 0; i < 3; i++ )
    {
      HTTPSampler urlConfigElement = new HTTPSampler();
      urlConfigElement.setPath( urls[ i ] );
      urlConfigElement.setMethod( "GET" );
      urlConfigElement.setName( "Url Config" );

      HttpRequestConfig config = new HttpRequestConfig( "Http Request " + i, urlConfigElement );
      configs.add( config );
    }
    wholeConfig.setConfigs( configs );
    sampler.setProperty( new TestElementProperty( ListContentSplitPanel.CONFIG, wholeConfig ) );
  }

  @Test
  public void testSampleAllPass()
  {
    sampler.setProperty( Constants.RESULT_OPTION, Constants.ResultOption.ALLPASS.getOptionValue() );
    SampleResult result = sampler.sample( null );
    org.junit.Assert.assertEquals( 3, result.getSubResults().length );
    org.junit.Assert.assertEquals( result.isSuccessful(), false );
  }

  @Test
  public void testSampleOnePass()
  {
    sampler.setProperty( Constants.RESULT_OPTION, Constants.ResultOption.ONEPASS.getOptionValue() );
    SampleResult result = sampler.sample( null );
    org.junit.Assert.assertEquals( 3, result.getSubResults().length );
    org.junit.Assert.assertEquals( result.isSuccessful(), true );
  }
}
