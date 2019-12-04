package nl.han.ica.icss.parser_instructors;

import nl.han.ica.icss.Pipeline;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParserInstructorTest {

    private Pipeline parseCheck(final Path path, final String astExpected, boolean isValid) throws IOException {
        final String inputTest = Files.readString(path);
        final Pipeline pipeline = new Pipeline();
        pipeline.parseString(inputTest);
        assertEquals(astExpected, pipeline.getAST().toString());
        assertTrue(pipeline.isParsed());
        assertEquals(pipeline.check(), isValid);
        assertEquals(pipeline.isChecked(), isValid);
        return pipeline;
    }

    private void parseCheckTransformGenerate(final Path path, final String astExpected, final String cssExpected) throws IOException {
        final Pipeline pipeline = parseCheck(path, astExpected, true);
        pipeline.transform();
        assertTrue(pipeline.isTransformed());
        assertTrue(pipeline.getErrors().isEmpty());
        assertEquals(cssExpected, pipeline.generate());
        assertTrue(pipeline.getErrors().isEmpty());
    }

    @Test
    void testLevel0() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("level0.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][Pixel literal (500)|]]][Stylerule|[TagSelector a|][Declaration|[Property: (color)|][Color literal (#ff0000)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Pixel literal (520)|]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testLevel1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("level1.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (LinkColor)|[VariableReference (LinkColor)|][Color literal (#ff0000)|]][VariableAssignment (ParWidth)|[VariableReference (ParWidth)|][Pixel literal (500)|]][VariableAssignment (AdjustColor)|[VariableReference (AdjustColor)|][Bool Literal (TRUE)|]][VariableAssignment (UseLinkColor)|[VariableReference (UseLinkColor)|][Bool Literal (FALSE)|]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][VariableReference (ParWidth)|]]][Stylerule|[TagSelector a|][Declaration|[Property: (color)|][VariableReference (LinkColor)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Pixel literal (520)|]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testLevel2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("level2.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (LinkColor)|[VariableReference (LinkColor)|][Color literal (#ff0000)|]][VariableAssignment (ParWidth)|[VariableReference (ParWidth)|][Pixel literal (500)|]][VariableAssignment (AdjustColor)|[VariableReference (AdjustColor)|][Bool Literal (TRUE)|]][VariableAssignment (UseLinkColor)|[VariableReference (UseLinkColor)|][Bool Literal (FALSE)|]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][VariableReference (ParWidth)|]]][Stylerule|[TagSelector a|][Declaration|[Property: (color)|][VariableReference (LinkColor)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Add|[VariableReference (ParWidth)|][Multiply|[Scalar literal (2)|][Pixel literal (10)|]]]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testLevel3() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("level3.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (LinkColor)|[VariableReference (LinkColor)|][Color literal (#ff0000)|]][VariableAssignment (ParWidth)|[VariableReference (ParWidth)|][Pixel literal (500)|]][VariableAssignment (AdjustColor)|[VariableReference (AdjustColor)|][Bool Literal (TRUE)|]][VariableAssignment (UseLinkColor)|[VariableReference (UseLinkColor)|][Bool Literal (FALSE)|]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][VariableReference (ParWidth)|]][If_Clause|[VariableReference (AdjustColor)|][Declaration|[Property: (color)|][Color literal (#124532)|]][If_Clause|[VariableReference (UseLinkColor)|][Declaration|[Property: (background-color)|][VariableReference (LinkColor)|]]]]][Stylerule|[TagSelector a|][Declaration|[Property: (color)|][VariableReference (LinkColor)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Add|[VariableReference (ParWidth)|][Pixel literal (20)|]]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]][Declaration|[Property: (background-color)|][VariableReference (LinkColor)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA01T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA01-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][Pixel literal (500)|]]][Stylerule|[TagSelector a|][Declaration|[Property: (color)|][Color literal (#ff0000)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Pixel literal (520)|]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA01T2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA01-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[ClassSelector .verylongclass-dash|][Declaration|[Property: (height)|][Pixel literal (300)|]]][Stylerule|[TagSelector xml|][Declaration|[Property: (color)|][Color literal (#ff00ff)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA01T3() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA01-T3.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[IdSelector #ffeeee|][Declaration|[Property: (width)|][Pixel literal (100)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA02T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA02-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (LinkColor)|[VariableReference (LinkColor)|][Color literal (#ff0000)|]][VariableAssignment (ParWidth)|[VariableReference (ParWidth)|][Pixel literal (500)|]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][VariableReference (ParWidth)|]]][Stylerule|[TagSelector a|][Declaration|[Property: (color)|][VariableReference (LinkColor)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Pixel literal (520)|]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA02T2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA02-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (LinkColor)|[VariableReference (LinkColor)|][Color literal (#ff0000)|]][VariableAssignment (ParWidth)|[VariableReference (ParWidth)|][Pixel literal (500)|]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][VariableReference (ParWidth)|]]][Stylerule|[TagSelector a|][VariableAssignment (ExtraVar)|[VariableReference (ExtraVar)|][Pixel literal (10)|]][Declaration|[Property: (color)|][VariableReference (LinkColor)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Pixel literal (520)|]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA02T3() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA02-T3.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (LinkColor)|[VariableReference (LinkColor)|][Color literal (#ff0000)|]][VariableAssignment (ParWidth)|[VariableReference (ParWidth)|][Pixel literal (500)|]][VariableAssignment (ParWidth)|[VariableReference (ParWidth)|][VariableReference (LinkColor)|]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][VariableReference (ParWidth)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA03T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA03-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (LinkColor)|[VariableReference (LinkColor)|][Color literal (#ff0000)|]][VariableAssignment (ParWidth)|[VariableReference (ParWidth)|][Pixel literal (500)|]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][VariableReference (ParWidth)|]]][Stylerule|[TagSelector a|][Declaration|[Property: (color)|][VariableReference (LinkColor)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Add|[VariableReference (ParWidth)|][Multiply|[Scalar literal (2)|][Pixel literal (10)|]]]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA03T2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA03-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (LinkColor)|[VariableReference (LinkColor)|][Color literal (#ff0000)|]][VariableAssignment (ParWidth)|[VariableReference (ParWidth)|][Add|[Pixel literal (500)|][Pixel literal (10)|]]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][VariableReference (ParWidth)|]]][Stylerule|[TagSelector a|][Declaration|[Property: (color)|][VariableReference (LinkColor)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Add|[VariableReference (ParWidth)|][Multiply|[Scalar literal (2)|][Pixel literal (10)|]]]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA03T3() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA03-T3.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (LinkColor)|[VariableReference (LinkColor)|][Color literal (#ff0000)|]][VariableAssignment (ParWidth)|[VariableReference (ParWidth)|][Pixel literal (500)|]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][Subtract|[Add|[Pixel literal (2)|][Multiply|[VariableReference (ParWidth)|][Scalar literal (5)|]]][Pixel literal (100)|]]]][Stylerule|[TagSelector a|][Declaration|[Property: (color)|][VariableReference (LinkColor)|]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA04T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA04-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (AdjustColor)|[VariableReference (AdjustColor)|][Bool Literal (TRUE)|]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][Pixel literal (10)|]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]][If_Clause|[VariableReference (AdjustColor)|][VariableAssignment (UseLinkColor)|[VariableReference (UseLinkColor)|][Bool Literal (FALSE)|]][If_Clause|[VariableReference (UseLinkColor)|][Declaration|[Property: (color)|][Color literal (#123123)|]]]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA04T2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA04-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (AdjustColor)|[VariableReference (AdjustColor)|][Bool Literal (TRUE)|]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][Pixel literal (10)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Pixel literal (20)|]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]][If_Clause|[VariableReference (AdjustColor)|][VariableAssignment (UseLinkColor)|[VariableReference (UseLinkColor)|][Bool Literal (TRUE)|]][If_Clause|[VariableReference (UseLinkColor)|][Declaration|[Property: (color)|][Color literal (#123123)|]]]]]]";
        parseCheck(path, astExpected, true);
    }

    @Test
    void testPA04T3() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA04-T3.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Pixel literal (20)|]][If_Clause|[VariableReference (UnknownVariable)|][Declaration|[Property: (color)|][Color literal (#123123)|]]]]]";
        parseCheck(path, astExpected, false);
    }

    @Test
    void testCH01T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("CH01-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[TagSelector span|][Declaration|[Property: (color)|][VariableReference (LinkColor)|]]]]";
        parseCheck(path, astExpected, false);
    }

    @Test
    void testCH01T2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("CH01-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (LinkColor)|[VariableReference (LinkColor)|][Color literal (#ff00ff)|]][VariableAssignment (LinkColorTwo)|[VariableReference (LinkColorTwo)|][VariableReference (LinkColor)|]][VariableAssignment (LinkColorThree)|[VariableReference (LinkColorThree)|][VariableReference (LankColor)|]][Stylerule|[TagSelector span|][Declaration|[Property: (color)|][VariableReference (LinkColor)|]]]]";
        parseCheck(path, astExpected, false);
    }

    @Test
    void testCH02T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("CH02-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[TagSelector a|][Declaration|[Property: (width)|][Add|[Add|[Pixel literal (20)|][Pixel literal (10)|]][Scalar literal (2)|]]]]]";
        parseCheck(path, astExpected, false);
    }

    @Test
    void testCH02T2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("CH02-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (Width)|[VariableReference (Width)|][Pixel literal (10)|]][Stylerule|[TagSelector a|][Declaration|[Property: (width)|][Multiply|[Pixel literal (20)|][VariableReference (Width)|]]]]]";
        parseCheck(path, astExpected, false);
    }

    @Test
    void testCH03T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("CH03-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[TagSelector p|][Declaration|[Property: (color)|][Add|[Color literal (#000000)|][Color literal (#ff00ff)|]]]]]";
        parseCheck(path, astExpected, false);
    }

    @Test
    void testCH03T2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("CH03-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (Color)|[VariableReference (Color)|][Color literal (#ff00ff)|]][Stylerule|[TagSelector p|][Declaration|[Property: (width)|][Add|[Pixel literal (20)|][VariableReference (Color)|]]]]]";
        parseCheck(path, astExpected, false);
    }

    @Test
    void testCH04T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("CH04-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[TagSelector span|][Declaration|[Property: (color)|][Pixel literal (100)|]]]]";
        parseCheck(path, astExpected, false);
    }

    @Test
    void testCH04T2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("CH04-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (Color)|[VariableReference (Color)|][Color literal (#ff00ff)|]][Stylerule|[TagSelector span|][Declaration|[Property: (width)|][VariableReference (Color)|]]]]";
        parseCheck(path, astExpected, false);
    }

    @Test
    void testTR01T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("TR01-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[TagSelector p|][Declaration|[Property: (width)|][Add|[Pixel literal (100)|][Multiply|[Scalar literal (40)|][Pixel literal (2)|]]]]]]";
        final String cssExpected = "/* Generated from ICSS, do not edit */\n\n" +
                "p {\n" +
                "\twidth: 180px;\n" +
                "}\n";
        parseCheckTransformGenerate(path, astExpected, cssExpected);
    }

    @Test
    void testTR01T2() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("TR01-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (BaseWidth)|[VariableReference (BaseWidth)|][Pixel literal (2)|]][Stylerule|[TagSelector p|][Declaration|[Property: (width)|][Add|[Pixel literal (100)|][VariableReference (BaseWidth)|]]]]]";
        final String cssExpected = "/* Generated from ICSS, do not edit */\n" +
                "\n" +
                "p {\n" +
                "\twidth: 102px;\n" +
                "}\n";
        parseCheckTransformGenerate(path, astExpected, cssExpected);
    }

    @Test
    void testTR01T3() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("TR01-T3.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (BaseWidth)|[VariableReference (BaseWidth)|][Pixel literal (2)|]][Stylerule|[TagSelector p|][VariableAssignment (BaseWidth)|[VariableReference (BaseWidth)|][Pixel literal (10)|]][Declaration|[Property: (width)|][Add|[Pixel literal (100)|][VariableReference (BaseWidth)|]]]][Stylerule|[TagSelector a|][Declaration|[Property: (width)|][VariableReference (BaseWidth)|]]]]";
        final String cssExpected = "/* Generated from ICSS, do not edit */\n\n" +
                "p {\n" +
                "\twidth: 110px;\n" +
                "}\n" +
                "a {\n" +
                "\twidth: 2px;\n" +
                "}\n";
        parseCheckTransformGenerate(path, astExpected, cssExpected);
    }

    @Test
    void testTR02T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("PA04-T2.icss")).toURI());
        final String astExpected = "[Stylesheet|[VariableAssignment (AdjustColor)|[VariableReference (AdjustColor)|][Bool Literal (TRUE)|]][Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][Pixel literal (10)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Pixel literal (20)|]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]][If_Clause|[VariableReference (AdjustColor)|][VariableAssignment (UseLinkColor)|[VariableReference (UseLinkColor)|][Bool Literal (TRUE)|]][If_Clause|[VariableReference (UseLinkColor)|][Declaration|[Property: (color)|][Color literal (#123123)|]]]]]]";
        final String cssExpected = "/* Generated from ICSS, do not edit */\n\n" +
                "p {\n" +
                "\tbackground-color: #ffffff;\n" +
                "\twidth: 10px;\n" +
                "}\n" +
                "#menu {\n" +
                "\twidth: 20px;\n" +
                "}\n" +
                ".menu {\n" +
                "\tcolor: #000000;\n" +
                "\tcolor: #123123;\n" +
                "}\n";
        parseCheckTransformGenerate(path, astExpected, cssExpected);
    }

    @Test
    void testGE01T1() throws IOException, URISyntaxException {
        final Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("GE01-T1.icss")).toURI());
        final String astExpected = "[Stylesheet|[Stylerule|[TagSelector p|][Declaration|[Property: (background-color)|][Color literal (#ffffff)|]][Declaration|[Property: (width)|][Pixel literal (10)|]]][Stylerule|[IdSelector #menu|][Declaration|[Property: (width)|][Pixel literal (100)|]]][Stylerule|[ClassSelector .menu|][Declaration|[Property: (color)|][Color literal (#000000)|]]]]";
        final String cssExpected = "/* Generated from ICSS, do not edit */\n\n" +
                "p {\n" +
                "\tbackground-color: #ffffff;\n" +
                "\twidth: 10px;\n" +
                "}\n" +
                "#menu {\n" +
                "\twidth: 100px;\n" +
                "}\n" +
                ".menu {\n" +
                "\tcolor: #000000;\n" +
                "}\n";
        parseCheckTransformGenerate(path, astExpected, cssExpected);
    }
}
