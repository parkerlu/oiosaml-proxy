package dk.itst.oiosaml;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import dk.itst.oiosaml.configuration.SystemConfiguration;
import dk.itst.oiosaml.sp.service.DispatcherServlet;
import dk.itst.oiosaml.sp.service.SPFilter;
import dk.itst.oiosaml.sp.service.session.SessionDestroyListener;

@SpringBootApplication
public class OiosamlProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(OiosamlProxyApplication.class, args);
	}
	
	@Bean
	public FilterRegistrationBean getSPFilter(@Value("${oiosaml-j.home}") String homeDir) {
		// 注入配置文件目录属性
		SystemConfiguration.setHomeDir(homeDir);
		SPFilter filter = new SPFilter();
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(filter);
		// 必修设置，因为当只有一个filter时，默认会拦截 /*
		registrationBean.addUrlPatterns("/nofilter");
		return registrationBean;
	}
	
	@Bean
	public ServletRegistrationBean getSAMLDispatcherServlet() {
		DispatcherServlet servlet = new DispatcherServlet();
		ServletRegistrationBean registrationBean = new ServletRegistrationBean();
		registrationBean.setServlet(servlet);
		registrationBean.setLoadOnStartup(1);
		registrationBean.setName("samlDispatcherServlet");
		registrationBean.addUrlMappings("/saml/*");
		return registrationBean;
	}
	
	@Bean
	public ServletListenerRegistrationBean<SessionDestroyListener> getSessionDestroyListener() {
		SessionDestroyListener listener = new SessionDestroyListener();
		ServletListenerRegistrationBean<SessionDestroyListener> registrationBean = new ServletListenerRegistrationBean<SessionDestroyListener>();
		registrationBean.setListener(listener);
		return registrationBean;
	}
}
