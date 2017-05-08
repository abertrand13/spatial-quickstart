import org.virtualized._
import spatial._

object SwitchVideo extends SpatialApp {
  import IR._
  override val target = targets.DE1

  // This is the IP Address of the 109-04 board: 172.24.89.108
  // Define your types, classes, etc. here

  type UInt8 = FixPt[FALSE,_8,_0]
  type UInt6 = FixPt[FALSE,_6,_0]
  type UInt5 = FixPt[FALSE,_5,_0]
  type UInt3 = FixPt[FALSE,_3,_0]
  type UInt2 = FixPt[FALSE,_2,_0]

  @struct case class bit24(b: UInt3, B: UInt5, g: UInt2, G: UInt6, r: UInt3, R: UInt5)
  @struct case class bit16(b: UInt5, g: UInt6, r: UInt5)

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

	val pixel = imgIn.value()

	io1 := swInput.value()
	imgOut := bit16(pixel.B.to[UInt5], pixel.G.to[UInt6], pixel.R.to[UInt5])
    }
	
	// Not sure if this is needed (with Pipe)
	// val switch_value = getArg(io1)
  }
}
