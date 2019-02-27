package cn.notemi.config;

import cn.notemi.controller.APIController;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiListingScanningContext;
import springfox.documentation.spring.web.scanners.ApiModelReader;

import java.util.*;

/**
 * Swagger 设置登录获取token接口
 */
public class FormLoginOperations extends ApiListingScanner
{
    @Autowired
    private TypeResolver typeResolver;

    @Autowired
    public FormLoginOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader, DocumentationPluginsManager pluginsManager)
    {
        super(apiDescriptionReader, apiModelReader, pluginsManager);
    }

    @Override
    public Multimap<String, ApiListing> scan(ApiListingScanningContext context)
    {
        final Multimap<String, ApiListing> def = super.scan(context);

        final List<ApiDescription> apis = new LinkedList<>();

        final List<Operation> operations = new ArrayList<>();

        Set<String> tags = new HashSet<>();
        tags.add("授权相关API");
        operations.add(new OperationBuilder(new CachingOperationNameGenerator())
            .method(HttpMethod.POST)
            .uniqueId("login")
            .tags(tags)
            .parameters(Arrays.asList(new ParameterBuilder()
                .name("x-auth-username")
                .description("The username")
                .parameterType("header")
                .type(typeResolver.resolve(String.class))
                .modelRef(new ModelRef("string"))
                .build(), 
                new ParameterBuilder()
                .name("x-auth-password")
                .description("The password")
                .parameterType("header")
                .type(typeResolver.resolve(String.class))
                .modelRef(new ModelRef("string"))
                .build()))
            .summary("登录获取token")
            .notes("Here you can log in")
            .build());
        apis.add(new ApiDescription(APIController.AUTH_LOCAL_URL, "Authentication documentation", operations, false));

        def.put("authorization", new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering())
            .apis(apis)
            .description("Custom authentication")
            .build());

        return def;
    }
}