import org.virtualized._
import spatial._

object SwitchVideo extends SpatialApp {
  import IR._
  override val target = targets.DE1

  // This is the IP Address of the 109-04 board: 172.24.89.108
  // Define your types, classes, etc. here

  type UInt8 = FixPt[FALSE,_8,_0]
  type UInt6 = FixPt[FALSE,_6,_0]

  @struct case class bit24(b: UInt8, g: UInt8, r: UInt8)
  @struct case class bit16(b: UInt6, g: UInt6, r: UInt6)

  @virtualize 
  def main() { 
    // Define your IOs here

	val io1 = HostIO[Int]
	val switch = target.SliderSwitch
	val swInput = StreamIn[Int](switch)

	val imgIn = StreamIn[bit24](target.VideoCamera)
	val imgOut = StreamOut[bit16](target.VGA)
	
    Accel(*) {
      	// Write your hardware description here
		// All necessary val's go here

		Stream(*) { _ =>
			val pixel = imgIn.value()

			Pipe { io1 := swInput.value() }
			Pipe { imgOut := bit16(pixel.b.to[UInt6], pixel.g.to[UInt6], pixel.r.to[UInt6]) }
		}
    }
	
	val switch_value = getArg(io1)
	println("Value: " + switch_value)
  }
}
