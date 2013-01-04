package de.schauderhaft.degraph.categorizer

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import com.jeantessier.dependency.ClassNode
import com.jeantessier.dependency.PackageNode
import de.schauderhaft.degraph.analysis.Node



@RunWith(classOf[JUnitRunner])
class InternalClassCategorizerTest extends FunSuite with ShouldMatchers {
    val cat = InternalClassCategorizer
    test("categorizes a simple class as it self") {
        val classNode = Node("Class","de.schauderhaft.SomeClass")
        cat(classNode) should be (classNode)
    }
    
    test("categorizes a inner class as the outer class") {
    	val innerClassNode = Node("Class","de.schauderhaft.SomeClass$SomeInnerClass")
    	val outerClassNode = Node("Class","de.schauderhaft.SomeClass")
        cat(innerClassNode) should be (outerClassNode)
    }
    test("categorizes a nested inner class as the outer most class") {
    	val innerClassNode = Node("Class","de.schauderhaft.SomeClass$SomeInnerClass$Of$Another$InnerClass")
    	val outerClassNode = Node("Class","de.schauderhaft.SomeClass")
        cat(innerClassNode) should be (outerClassNode)
    }
}