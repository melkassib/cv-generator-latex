[![Build](https://github.com/melkassib/cv-generator-latex/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/melkassib/cv-generator-latex/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=melkassib%3Acv-generator-latex&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=melkassib:cv-generator-latex)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=melkassib%3Acv-generator-latex&metric=coverage)](https://sonarcloud.io/summary/new_code?id=melkassib:cv-generator-latex)
[![Maven Central Version](https://img.shields.io/maven-central/v/com.melkassib/cv-generator-latex)](https://central.sonatype.com/artifact/com.melkassib/cv-generator-latex)

# DSL for AltaCV/AwesomeCV Résumés

Generate professional resumes with ease using [AltaCV](https://github.com/liantze/AltaCV) and [AwesomeCV](https://github.com/posquit0/Awesome-CV) LaTeX templates in a type-safe Kotlin DSL.

## Why I Built This

This started as a side project to explore building DSLs with Kotlin. Many of my friends are amazed by the quality of LaTeX in the compiled PDF but struggle with LaTeX. So, I built this library to make LaTeX-based CV generation easier and more customizable for developers.

## Features:

✅ **Template-Based CV Generation** – Supports LaTeX templates (AltaCV/AwesomeCV) for structured CV generation.

✅ **Kotlin & Java Support** – Designed for use in Kotlin, but Java is also supported.

✅ **Customizable Templates** – Define your own LaTeX structures for flexibility (tuning parameters without altering template code).

✅ **Separation of Data & Presentation** – Focus on your résumé data, forget LaTeX commands

✅ **Simple Integration** – Can be used in web services or standalone apps.

✅ **JSON & YAML Serialization** – Save your résumé in JSON and YAML representations, useful for web services

## Usage

### Adding the dependency

This package is available on Maven central.

#### Maven:

```xml
<dependency>
    <groupId>com.melkassib</groupId>
    <artifactId>cv-generator-latex</artifactId>
    <version>0.1.0</version>
</dependency>
```

#### Gradle:

```kotlin
dependencies {
    implementation("com.melkassib:cv-generator-latex:0.1.0")
}
```

### Create your résumé

1. Create a repository based on this template: [cv-generator-latex-template](https://github.com/melkassib/cv-generator-latex-template/)
2. Start writing your résumé using your preferred language:
    - Kotlin: `src/main/kotlin`
    - Java: `src/main/java`
3. Use the following Overleaf templates to compile your generated LaTeX résumé:
    - [AltaCV](https://github.com/liantze/AltaCV): Template [Link](https://www.overleaf.com/latex/templates/altacv-template/trgqjpwnmtgv) by LianTze Lim (liantze@gmail.com)
    - [AwesomeCV](https://github.com/posquit0/Awesome-CV): Template [Link](https://www.overleaf.com/latex/templates/awesome-cv/tvmzpvdjfqxp) by Claud D. Park (posquit0.bj@gmail.com)

## Examples

You can find the generated files in the [docs/examples](docs/examples) folder.

### AltaCV résumé using Kotlin DSL
```kotlin
altacv {
    config {
        photoShape = PhotoShape.NORMAL
        theme = PredefinedColorPalette.THEME1
    }

    header {
        tagline = "Your Position or Tagline Here"
        photo = Photo(2.8, "Globe_High.png")
        userInfo = AltaCVUserInfo("Your Name Here", linkedSetOf(
            Email("your_name@email.com"),
            Phone("000-00-0000"),
            MailAddress("Address, Street, 00000 Country"),
            Location("Location, COUNTRY"),
            HomePage("www.homepage.com"),
            Twitter("@twitterhandle"),
            LinkedIn("your_id"),
            Github("your_id"),
            Orcid("0000-0000-0000-0000"),
            UserInfoField("gitlab", "\\faGitlab", "https://gitlab.com/", "your_id")
        ))
    }

    sections {
        section("Experience", firstColumn(1), Divider) {
            contents {
                event("Job Title 1") {
                    holder = "Company 1"
                    location = "Location"
                    duration = eventDurationStr("Month XXXX", "Ongoing")
                    description = listOf(
                        Item("Job description 1"),
                        Item("Job description 2"),
                        Item("Job description 3", false)
                    )
                }

                event("Job Title 2") {
                    holder = "Company 2"
                    location = "Location"
                    duration = eventDurationDate("2023-10", "2023-10")
                    description = listOf(Item("Item1"))
                }

                event("Job Title 3") {
                    holder = "Company 3"
                    location = "Location"
                }
            }
        }

        section("Projects", firstColumn(2), separator = Divider) {
            contents {
                event("Project 1") {
                    holder = "Funding agency/institution"
                    description = listOf(Item("Details"))
                }

                event("Project 1") {
                    holder = "Funding agency/institution"
                    duration = eventDurationStr("Project duration")
                    description = listOf(Item(" A short abstract would also work.", withBullet = false))
                }
            }
        }

        section("A day of my life", firstColumn(3)) {
            contents {
                wheelchart(1.5, 0.5) {
                    item(6, 8, "accent!30", "Sleep,\\\\beautiful sleep")
                    item(3, 8, "accent!40", "Hopeful novelist by night")
                    item(8, 8, "accent!60", "Daytime job")
                    item(2, 10, "accent", "Sports and relaxation")
                    item(5, 8, "accent!20", "Spending time with family")
                }
                content("\\newpage")
            }
        }

        section("My Life Philosophy", secondColumn(1)) {
            contents {
                quote("Something smart or heartfelt, preferably in one sentence.")
            }
        }

        section("Most Proud of", secondColumn(2), ignored = false) {
            contents {
                achievement("faTrophy", "Fantastic Achievement", "and some details about it")
                achievement("faHeartbeat", "Another achievement", "more details about it of course")
                achievement("faHeartbeat", "Another achievement", "more details about it of course")
            }
        }

        section("Strengths", secondColumn(3)) {
            contents {
                tag("Hard-working")
                tag("Eye for detail")
                content(NewLine)

                tag("Motivator & Leader")
                content(Divider)

                tag("C++")
                tag("Embedded Systems")
                content(NewLine)

                tag("Statistical Analysis")
            }
        }

        section("Languages", secondColumn(4)) {
            contents {
                skill("Arabic", "Native/Bilingual")
                skill("English", "Professional working proficiency")
                skill("Spanish", "Limited working proficiency")
                skill("German", 2.0)
            }
        }

        section("Education", secondColumn(5), separator = Divider) {
            contents {
                event("Ph.D. in Your Discipline") {
                    holder = "Your University"
                    duration =  eventDurationDate("2002-09", "2006-06")
                    description = listOf(Item("Thesis title: Wonderful Research",  false))
                }

                event("M.Sc. in Your Discipline") {
                    holder = "Your University"
                    duration = eventDurationDate("2001-09", "2002-06")
                }

                event("B.Sc. in Your Discipline") {
                    holder = "Stanford University"
                    duration = eventDurationDate("1998-09", "2001-06")
                }
            }
        }
    }
}
```

### AwesomeCV résumé using Kotlin DSL
```kotlin
awesomecv {
    config {
        colorTheme = ColorTheme.ORANGE
        isSectionHighlighted = true
        headerSocialSeparator = "\\cdotp"
    }

    header {
        photo = Photo(PhotoShape.RECTANGLE, PhotoEdge.NO_EDGE, path = "profile")
        quote = "Be the change that you want to see in the world."

        user {
            firstName = "John"
            lastName = "Dupont"
            personalInfo = linkedSetOf(
                Position("Developer \\enskip\\cdotp\\enskip Cloud Engineer"),
                MailAddress("Address, Street, City"),
                Phone("(+212) 000-000-000"),
                Email("contact@email.com"),
                HomePage("www.homepage.com"),
                Github("your_id"),
                LinkedIn("your_id"),
                Gitlab("your_id"),
                Twitter("@your_id"),
                Skype("your_id"),
                Reddit("your_id"),
                Medium("your_id"),
                StackOverFlow("SO-id", "SO-name"),
                GoogleScholar("googlescholar-id", "name-to-display"),
                ExtraInfo("extra information"),
            )
        }
    }

    footer {
        left = "\\today"
        center = "John Dupont $\\sim$$\\sim$ $\\cdot$ $\\sim$$\\sim$ Résumé"
        right = "\\thepage"
    }

    sections {
        section("Summary") {
            contents {
                paragraph("This is a paragraph")
            }
        }

        section("Work Experience") {
            contents {
                event("Software Engineer") {
                    holder = "TechNova Solutions"
                    location = "New York, USA"
                    duration = eventDurationStr("Jan. 2022", "Present")
                    description = listOf(
                        Item("Developed scalable microservices architecture for cloud applications."),
                        Item("Reduced system downtime by 40% by implementing automated monitoring and alerting."),
                        Item("Led a team of 5 engineers to build a real-time analytics dashboard using Kafka and Elasticsearch.")
                    )
                }

                event("Backend Developer") {
                    holder = "CodeWave Inc."
                    location = "San Francisco, USA"
                    duration = eventDurationStr("Jun. 2019", "Dec. 2021")
                    description = listOf(
                        Item("Designed RESTful APIs that handled over 1M requests per day."),
                        Item("Optimized database queries, improving response times by 50%."),
                        Item("Implemented CI/CD pipelines using Jenkins and Docker to streamline deployments.")
                    )
                }
            }
        }

        section("Honors & Awards") {
            contents {
                honors("International Awards") {
                    honor("Winner", "Google Cloud Hackathon", "Online", "2022")
                    honor("Top 5 Finalist", "Microsoft AI Challenge", "Seattle, USA", "2021")
                }

                content("\\medskip")

                honors("Domestic Awards") {
                    honor("1st Place", "National Coding Championship", "New York, USA", "2020")
                }
            }
        }
    }
}
```

## Credits
- [AltaCV](https://github.com/liantze/AltaCV): LaTeX template by LianTze Lim (liantze@gmail.com)
- [AwesomeCV](https://github.com/posquit0/Awesome-CV): LaTeX template by posquit0 (posquit0.bj@gmail.com)
