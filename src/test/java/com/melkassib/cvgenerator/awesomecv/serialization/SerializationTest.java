package com.melkassib.cvgenerator.awesomecv.serialization;

import com.melkassib.cvgenerator.awesomecv.domain.*;
import com.melkassib.cvgenerator.common.domain.*;
import com.networknt.schema.InputFormat;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.melkassib.cvgenerator.common.domain.EventPeriodString.eventDurationStr;
import static com.melkassib.cvgenerator.common.utils.ResumeHelper.firstColumn;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class SerializationTest {

    private static JsonSchema jsonSchema;

    @BeforeAll
    static void beforeAll() {
        jsonSchema = loadJsonSchema();
    }

    @Test
    void serialize_awesomecv_resume() {
        final var photo = new Photo(PhotoShape.RECTANGLE, PhotoEdge.NO_EDGE, PhotoDirection.RIGHT, "profile");
        final var quote = "Be the change that you want to see in the world.";

        final var userInfo = getUserInfo();

        final var config = new AwesomeCVConfig(ColorTheme.ORANGE, true, "\\cdotp");
        final var header = new AwesomeCVHeader(HeaderAlignment.CENTER, userInfo, photo, quote);
        final var footer = new AwesomeCVFooter("\\today", "John Dupont $\\sim$$\\sim$ $\\cdot$ $\\sim$$\\sim$ Résumé", "\\thepage");

        // --------------------- Summary ---------------------

        final var paragraph = new Paragraph("Nulla blandit sapien ligula");

        final var section1 = new Section("Summary", firstColumn(1), List.of(paragraph));

        // --------------------- Work Experience ---------------------

        final var s2Event1 = new Entry();
        s2Event1.setTitle("Software Engineer");
        s2Event1.setHolder("TechNova Solutions");
        s2Event1.setLocation("New York, USA");
        s2Event1.setDuration(eventDurationStr("Jan. 2022", "Present"));
        s2Event1.setDescription(List.of(
            new Item("Developed scalable microservices architecture for cloud applications."),
            new Item("Reduced system downtime by 40% by implementing automated monitoring and alerting."),
            new Item("Led a team of 5 engineers to build a real-time analytics dashboard using Kafka and Elasticsearch.")
        ));

        final var s2Event2 = new Entry();
        s2Event2.setTitle("Backend Developer");
        s2Event2.setHolder("CodeWave Inc.");
        s2Event2.setLocation("San Francisco, USA");
        s2Event2.setDuration(eventDurationStr("Jun. 2019", "Dec. 2021"));
        s2Event2.setDescription(List.of(
            new Item("Designed RESTful APIs that handled over 1M requests per day."),
            new Item("Optimized database queries, improving response times by 50%."),
            new Item("Implemented CI/CD pipelines using Jenkins and Docker to streamline deployments.")
        ));

        final var section2 = new Section("Work Experience", firstColumn(2), List.of(s2Event1, s2Event2));

        // --------------------- Honors & Awards ---------------------

        final var honorList1 = new HonorList("International Awards", List.of(
            new HonorItem("Winner", "Google Cloud Hackathon", "Online", "2022"),
            new HonorItem("Top 5 Finalist", "Microsoft AI Challenge", "Seattle, USA", "2021")
        ));

        final var honorList2 = new HonorList("Domestic Awards", List.of(
            new HonorItem("1st Place", "National Coding Championship", "New York, USA", "2020")
        ));
        final var section3 = new Section("Honors & Awards", firstColumn(3), List.of(
            honorList1,
            new LatexContent("\\medskip"),
            Divider.INSTANCE,
            honorList2
        ));

        // --------------------- Resume ---------------------

        final var resume = new AwesomeCVResume(config, header, footer, List.of(section1, section2, section3));

        final var resumeJson = resume.toJson();

        final var assertions = jsonSchema.validate(resumeJson, InputFormat.JSON);
        assertThat(assertions, hasSize(0));

        assertThat(resumeJson, hasJsonPath("$.config.colorTheme", equalTo("ORANGE")));
        assertThat(resumeJson, hasJsonPath("$.config.isSectionHighlighted", equalTo(true)));
        assertThat(resumeJson, hasJsonPath("$.config.headerSocialSeparator", equalTo("\\cdotp")));
        assertThat(resumeJson, hasJsonPath("$.header.alignment", equalTo("CENTER")));
        assertThat(resumeJson, hasJsonPath("$.header.quote", equalTo("Be the change that you want to see in the world.")));
        assertThat(resumeJson, hasJsonPath("$.header.photo.shape", equalTo("RECTANGLE")));
        assertThat(resumeJson, hasJsonPath("$.header.photo.edge", equalTo("NO_EDGE")));
        assertThat(resumeJson, hasJsonPath("$.header.photo.direction", equalTo("RIGHT")));
        assertThat(resumeJson, hasJsonPath("$.header.photo.path", equalTo("profile")));
        assertThat(resumeJson, hasJsonPath("$.header.userInfo.firstName", equalTo("John")));
        assertThat(resumeJson, hasJsonPath("$.header.userInfo.lastName", equalTo("Dupont")));
        assertThat(resumeJson, hasJsonPath("$.header.userInfo.personalInfo.length()", equalTo(15)));
        assertThat(resumeJson, hasJsonPath("$.footer.left", equalTo("\\today")));
        assertThat(resumeJson, hasJsonPath("$.footer.center", equalTo("John Dupont $\\sim$$\\sim$ $\\cdot$ $\\sim$$\\sim$ Résumé")));
        assertThat(resumeJson, hasJsonPath("$.footer.right", equalTo("\\thepage")));
        assertThat(resumeJson, hasJsonPath("$.sections.length()", equalTo(3)));
    }

    @Test
    void serialize_default_resume() {
        final var resume = new AwesomeCVResume();

        final var resumeJson = resume.toJson();

        final var assertions = jsonSchema.validate(resumeJson, InputFormat.JSON);
        assertThat(assertions, hasSize(0));

        assertThat(resumeJson, hasJsonPath("$.config.colorTheme", equalTo("RED")));
        assertThat(resumeJson, hasJsonPath("$.config.isSectionHighlighted", equalTo(true)));
        assertThat(resumeJson, hasJsonPath("$.config.headerSocialSeparator", equalTo("\\textbar")));
        assertThat(resumeJson, hasJsonPath("$.header.alignment", equalTo("CENTER")));
        assertThat(resumeJson, hasJsonPath("$.header.quote", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.header.photo", nullValue()));
        assertThat(resumeJson, hasJsonPath("$.header.userInfo", nullValue()));
        assertThat(resumeJson, hasJsonPath("$.footer.left", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.footer.center", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.footer.right", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.sections.length()", equalTo(0)));
    }

    @Test
    void serialize_resume_with_config() {
        final var config = new AwesomeCVConfig(ColorTheme.ORANGE, false);
        final var resume = new AwesomeCVResume(config);

        final var resumeJson = resume.toJson();

        final var assertions = jsonSchema.validate(resumeJson, InputFormat.JSON);
        assertThat(assertions, hasSize(0));

        assertThat(resumeJson, hasJsonPath("$.config.colorTheme", equalTo("ORANGE")));
        assertThat(resumeJson, hasJsonPath("$.config.isSectionHighlighted", equalTo(false)));
        assertThat(resumeJson, hasJsonPath("$.config.headerSocialSeparator", equalTo("\\textbar")));
        assertThat(resumeJson, hasJsonPath("$.header.alignment", equalTo("CENTER")));
        assertThat(resumeJson, hasJsonPath("$.header.quote", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.header.photo", nullValue()));
        assertThat(resumeJson, hasJsonPath("$.header.userInfo", nullValue()));
        assertThat(resumeJson, hasJsonPath("$.footer.left", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.footer.center", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.footer.right", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.sections.length()", equalTo(0)));
    }

    @Test
    void serialize_resume_with_config_and_header() {
        final var config = new AwesomeCVConfig(ColorTheme.ORANGE, false);
        final var header = new AwesomeCVHeader(HeaderAlignment.RIGHT);
        final var resume = new AwesomeCVResume(config, header);

        final var resumeJson = resume.toJson();

        final var assertions = jsonSchema.validate(resumeJson, InputFormat.JSON);
        assertThat(assertions, hasSize(0));

        assertThat(resumeJson, hasJsonPath("$.config.colorTheme", equalTo("ORANGE")));
        assertThat(resumeJson, hasJsonPath("$.config.isSectionHighlighted", equalTo(false)));
        assertThat(resumeJson, hasJsonPath("$.config.headerSocialSeparator", equalTo("\\textbar")));
        assertThat(resumeJson, hasJsonPath("$.header.alignment", equalTo("RIGHT")));
        assertThat(resumeJson, hasJsonPath("$.header.quote", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.header.photo", nullValue()));
        assertThat(resumeJson, hasJsonPath("$.header.userInfo", nullValue()));
        assertThat(resumeJson, hasJsonPath("$.footer.left", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.footer.center", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.footer.right", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.sections.length()", equalTo(0)));
    }

    @Test
    void serialize_resume_with_config_and_header_and_footer() {
        final var config = new AwesomeCVConfig(ColorTheme.ORANGE, false);
        final var header = new AwesomeCVHeader(HeaderAlignment.RIGHT);
        final var footer = new AwesomeCVFooter("\\today", "John Dupont - Résumé", "\\thepage");
        final var resume = new AwesomeCVResume(config, header, footer);

        final var resumeJson = resume.toJson();

        final var assertions = jsonSchema.validate(resumeJson, InputFormat.JSON);
        assertThat(assertions, hasSize(0));

        assertThat(resumeJson, hasJsonPath("$.config.colorTheme", equalTo("ORANGE")));
        assertThat(resumeJson, hasJsonPath("$.config.isSectionHighlighted", equalTo(false)));
        assertThat(resumeJson, hasJsonPath("$.config.headerSocialSeparator", equalTo("\\textbar")));
        assertThat(resumeJson, hasJsonPath("$.header.alignment", equalTo("RIGHT")));
        assertThat(resumeJson, hasJsonPath("$.header.quote", emptyString()));
        assertThat(resumeJson, hasJsonPath("$.header.photo", nullValue()));
        assertThat(resumeJson, hasJsonPath("$.header.userInfo", nullValue()));
        assertThat(resumeJson, hasJsonPath("$.footer.left", equalTo("\\today")));
        assertThat(resumeJson, hasJsonPath("$.footer.center", equalTo("John Dupont - Résumé")));
        assertThat(resumeJson, hasJsonPath("$.footer.right", equalTo("\\thepage")));
        assertThat(resumeJson, hasJsonPath("$.sections.length()", equalTo(0)));
    }

    private static AwesomeCVUserInfo getUserInfo() {
        final var personalInfo = new LinkedHashSet<>(
            List.of(
                new Position("Developer \\enskip\\cdotp\\enskip Cloud Engineer"),
                new MailAddress("Address, Street, City"),
                new Phone("(+212) 000-000-000"),
                new Email("contact@email.com"),
                new HomePage("www.homepage.com"),
                new Github("your_id"),
                new LinkedIn("your_id"),
                new Gitlab("your_id"),
                new Twitter("@your_id"),
                new Skype("your_id"),
                new Reddit("your_id"),
                new Medium("your_id"),
                new StackOverFlow("SO-id", "SO-name"),
                new GoogleScholar("googlescholar-id", "name-to-display"),
                new ExtraInfo("extra information")
            )
        );

        return new AwesomeCVUserInfo("John", "Dupont", personalInfo);
    }

    private static JsonSchema loadJsonSchema() {
        final var factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        try (var schemaStream = Files.newInputStream(Path.of("schemas/1.0.0/awesomecv.schema.json"))) {
            return factory.getSchema(schemaStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON schema", e);
        }
    }

}
