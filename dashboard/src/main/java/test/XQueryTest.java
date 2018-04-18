/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.saxon.s9api.DOMDestination;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.w3c.dom.Document;

/**
 *
 * @author Lino
 */
public class XQueryTest {

    public static void main(String args[]) throws SaxonApiException {
        test1();
    }

    //set a value like sql statements in jpql ..
    public static void test2() throws SaxonApiException {
        Processor proc = new Processor(false);
        XQueryCompiler comp = proc.newXQueryCompiler();
        XQueryExecutable exp = comp.compile("declare variable $n external; for $i in 1 to $n return $i*$i");
        XQueryEvaluator qe = exp.load();
        qe.setExternalVariable(new QName("n"), new XdmAtomicValue(2));
        XdmValue result = qe.evaluate();
        int total = 0;
        for (XdmItem item : result) {
            total += ((XdmAtomicValue) item).getLongValue();
        }
        System.out.println("Total: " + total);
    }
//get the individual map values;

    public static void test1() throws SaxonApiException {
        Processor proc = new Processor(true);
        XQueryCompiler comp = proc.newXQueryCompiler();
        XQueryExecutable exp = comp.compile("for $prod at $count in doc(\"catalog.xml\")//product\n"
                + "where $prod/@dept = (\"ACC\", \"MEN\")\n"
                + "order by $prod/name\n"
                + "return <p>{$count}. {data($prod/name)}</p>");
        XQueryEvaluator qe = exp.load();
        System.out.println("output----------->" + qe.evaluate());

    }

//set the result to a key
    public static void test3() throws SaxonApiException {
        Processor proc = new Processor(true);
        XQueryCompiler comp = proc.newXQueryCompiler();
        XQueryExecutable exp = comp.compile("parse-json('{\"x\":1, \"y\":[3,4,5]}')");
        XQueryEvaluator qe = exp.load();
        qe.setContextItem(new XdmAtomicValue("apple"));
        XdmValue result = qe.evaluate();
        System.out.println("apple: " + result.toString());
    }

    //passing the value from one to another and creating the dom.
    public static void test4() throws SaxonApiException {
        Processor proc = new Processor(false);
        XQueryCompiler comp = proc.newXQueryCompiler();
        comp.declareNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        XQueryExecutable exp = comp.compile("<temp>{for $i in 1 to 20 return <e>{$i}</e>}</temp>");

        DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
        dfactory.setNamespaceAware(true);
        Document dom;

        try {
            dom = dfactory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new SaxonApiException(e);
        }

        exp.load().run(new DOMDestination(dom));

        XdmNode temp = proc.newDocumentBuilder().wrap(dom);

        exp = comp.compile("<out>{//e[xsd:integer(.) gt 10]}</out>");
        XQueryEvaluator qe = exp.load();
        qe.setContextItem(temp);
        System.out.println("output------->" + qe.evaluate());
    }
    
}
