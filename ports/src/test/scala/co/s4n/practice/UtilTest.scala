package co.s4n.practice

class UtilTest extends TestSpec {

  it should "compare strings successfully" in {
    val message: String = "Unit test dummy"
    val FINAL_MESSAGE: String = "Unit test dummy"

    message shouldBe FINAL_MESSAGE

  }

}
