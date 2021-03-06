load("//tools/bzl:junit.bzl", "junit_tests")

java_library(
    name = "elasticsearch_test_utils",
    testonly = 1,
    srcs = [
        "ElasticContainer.java",
        "ElasticTestUtils.java",
    ],
    visibility = ["//visibility:public"],
    deps = [
        "//java/com/google/gerrit/elasticsearch",
        "//java/com/google/gerrit/index",
        "//java/com/google/gerrit/server",
        "//lib:guava",
        "//lib:junit",
        "//lib/guice",
        "//lib/httpcomponents:httpcore",
        "//lib/jgit/org.eclipse.jgit:jgit",
        "//lib/testcontainers",
        "//lib/testcontainers:testcontainers-elasticsearch",
    ],
)

ELASTICSEARCH_DEPS = [
    ":elasticsearch_test_utils",
    "//java/com/google/gerrit/elasticsearch",
    "//java/com/google/gerrit/testing:gerrit-test-util",
    "//lib/guice",
    "//lib/jgit/org.eclipse.jgit:jgit",
]

QUERY_TESTS_DEP = "//javatests/com/google/gerrit/server/query/%s:abstract_query_tests"

TYPES = [
    "account",
    "change",
    "group",
    "project",
]

SUFFIX = "sTest.java"

ELASTICSEARCH_TESTS_V5 = {i: "ElasticV5Query" + i.capitalize() + SUFFIX for i in TYPES}

ELASTICSEARCH_TESTS_V6 = {i: "ElasticV6Query" + i.capitalize() + SUFFIX for i in TYPES}

ELASTICSEARCH_TESTS_V7 = {i: "ElasticV7Query" + i.capitalize() + SUFFIX for i in TYPES}

ELASTICSEARCH_TAGS = [
    "docker",
    "elastic",
    "exclusive",
]

[junit_tests(
    name = "elasticsearch_query_%ss_test_V5" % name,
    size = "large",
    srcs = [src],
    tags = ELASTICSEARCH_TAGS,
    deps = ELASTICSEARCH_DEPS + [QUERY_TESTS_DEP % name],
) for name, src in ELASTICSEARCH_TESTS_V5.items()]

[junit_tests(
    name = "elasticsearch_query_%ss_test_V6" % name,
    size = "large",
    srcs = [src],
    tags = ELASTICSEARCH_TAGS + ["flaky"],
    deps = ELASTICSEARCH_DEPS + [QUERY_TESTS_DEP % name],
) for name, src in ELASTICSEARCH_TESTS_V6.items()]

[junit_tests(
    name = "elasticsearch_query_%ss_test_V7" % name,
    size = "large",
    srcs = [src],
    tags = ELASTICSEARCH_TAGS + ["flaky"],
    deps = ELASTICSEARCH_DEPS + [QUERY_TESTS_DEP % name] + [
        "//lib/httpcomponents:httpasyncclient",
        "//lib/httpcomponents:httpclient",
    ],
) for name, src in ELASTICSEARCH_TESTS_V7.items()]

junit_tests(
    name = "elasticsearch_tests",
    size = "small",
    srcs = glob(
        ["*Test.java"],
        exclude = ["Elastic*Query*" + SUFFIX],
    ),
    tags = ["elastic"],
    deps = [
        "//java/com/google/gerrit/elasticsearch",
        "//java/com/google/gerrit/testing:gerrit-test-util",
        "//lib:guava",
        "//lib/guice",
        "//lib/jgit/org.eclipse.jgit:jgit",
        "//lib/truth",
    ],
)
