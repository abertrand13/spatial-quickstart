import org.virtualized._
import spatial._

object SwitchVideo extends SpatialApp {
  import IR._
  override val target = targets.DE1

  // Define your types, classes, etc. here

  @virtualize 
  def main() { 
    // Define your IOs here

    Accel(*) {
      // Write your hardware description here
    }
  }
}
