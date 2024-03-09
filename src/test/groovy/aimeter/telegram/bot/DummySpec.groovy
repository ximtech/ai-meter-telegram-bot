package aimeter.telegram.bot

import org.springframework.test.context.jdbc.Sql

@Sql(scripts = ['/test-data.sql'], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class DummySpec extends DatabaseSpecTemplate {
    
    def 'dummy test - should be ok'() {
        expect:
        1 == 1
    }
}
