package org.eclipse.wst.xml.xpath2.processor.internal.types;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import org.eclipse.wst.xml.xpath2.api.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.internal.XPathExpressionTestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ElementTypeTest extends XPathExpressionTestBase {
    private static URL doc;

    @BeforeAll
    static void setupDocument() {
        doc = ElementTypeTest.class.getResource(
            "element_type_whitespace_test.xml");
    }

    @Test
    void removesLeadingAndTrailingWhitespaceInTextOnlyContent() throws Exception {
        // given

        // when
        ResultSequence result = executeXPathExpression(
            doc,
            "//root/element_with_text_only_content");

        // then
        assertThat(result).isInstanceOfSatisfying(
            ElementType.class,
            el -> assertThat(el.getStringValue())
                .isEqualTo("text-only content with leading and trailing whitespaces"));
    }

    @Test
    void removesLeadingAndTrailingWhitespaceInMixedContent() throws Exception {
        // given

        // when
        ResultSequence result = executeXPathExpression(
            doc,
            "//root/element_with_mixed_content");

        // then
        assertThat(result).isInstanceOfSatisfying(
            ElementType.class,
            el -> assertThat(el.getStringValue())
                .isEqualTo("mixed content with leading and trailing whitespaces"));
    }
}