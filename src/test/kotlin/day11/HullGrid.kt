package day11

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HullGrid {

    @Test
    fun `a robot knows when it is on a white square`() {
        val white = 1L
        val robot = Robot(Channel(), Channel())

        robot.paint(white)

        assertThat(robot.grid[0]?.get(0)).isEqualTo(1)
    }

    @Test
    fun `a robot knows when it is on a black square`() {
        val black = 0L
        val robot = Robot(Channel(), Channel())

        robot.paint(black)

        assertThat(robot.grid[0]?.get(0)).isEqualTo(0)
    }

    @Test
    fun `a robot can turn right`() {
        val instructions = Channel<Long>()
        val camera = Channel<Long>()
        val robot = Robot(instructions, camera)

        GlobalScope.launch {
            readAndDiscardFromCamera(camera)
        }

        GlobalScope.launch {
            robot.run()
        }

        runBlocking {
            instructions.send(1)
            instructions.send(1)
            instructions.close()

            assertThat(robot.position).isEqualTo(Coordinate(1, 0))
        }
    }

    /**
     *
     *      >-
     *
     *    then
     *
     *      *v
     *       |
     *
     *    ending
     *
     *      **
     *       v
     *
     */
    @Test
    fun `a robot can turn right twice`() {
        val instructions = Channel<Long>()
        val camera = Channel<Long>()
        val robot = Robot(instructions, camera)

        GlobalScope.launch {
            readAndDiscardFromCamera(camera)
        }

        GlobalScope.launch {
            robot.run()
        }

        runBlocking {
            instructions.send(1)
            instructions.send(1)
            instructions.send(1)
            instructions.send(1)
            instructions.close()

            assertThat(robot.position).isEqualTo(Coordinate(1, -1))
        }
    }

    private suspend fun readAndDiscardFromCamera(camera: Channel<Long>) {
        for (image in camera) {
            println("received image $image")
        }
    }

    @Test
    fun `a robot sends a channel of camera output`() {
        val instructions = Channel<Long>()
        val camera = Channel<Long>()

        val robot = Robot(instructions, camera)

        val receivedPlateColours = mutableListOf<Long>()
        GlobalScope.launch {
            for (image in camera) {
                receivedPlateColours.add(image)
                if (receivedPlateColours.size == 3)
                    camera.close()
            }
        }

        GlobalScope.launch {
            robot.run()
        }

        runBlocking {
            instructions.send(1) //paint
            instructions.send(0) //move
            instructions.send(1) //p
            instructions.send(0) //m
            instructions.send(1) //p
            instructions.send(0) //m
            instructions.close()

            assertThat(receivedPlateColours).containsExactly(0, 0, 0)
        }
    }

    @Test
    fun `the robot receives a channel with multiple instructions`() {
        val instructions = Channel<Long>()

        val camera = Channel<Long>()
        val robot = Robot(instructions, camera)

        GlobalScope.launch {
            readAndDiscardFromCamera(camera)
        }

        GlobalScope.launch {
            robot.run()
        }

        runBlocking {
            instructions.send(1)
            instructions.send(0)
            instructions.send(0)
            instructions.send(1)
            instructions.send(0)
            instructions.send(1)

            assertThat(robot.grid).isEqualTo(
                mutableMapOf(
                    0L to mutableMapOf(-1L to 0L, 0L to 1L),
                    1L to mutableMapOf(-1L to 0L)
                )
            )
        }
    }

    @Test
    fun `a robot sees painted plates in a channel of camera output`() {
        val instructions = Channel<Long>()
        val camera = Channel<Long>()

        val robot = Robot(instructions, camera)

        val receivedPlateColours = mutableListOf<Long>()
        GlobalScope.launch {
            for (image in camera) {
                receivedPlateColours.add(image)
                if (receivedPlateColours.size == 5)
                    camera.close()
            }
        }

        GlobalScope.launch {
            robot.run()
        }

        runBlocking {
            instructions.send(1) //paint
            instructions.send(0) //move
            instructions.send(1) //p
            instructions.send(0) //m
            instructions.send(1) //p
            instructions.send(0) //m
            instructions.send(1) //p
            instructions.send(0) //m
            instructions.send(1) //p
            instructions.send(0) //m
            instructions.close()

            assertThat(receivedPlateColours).containsExactly(0, 0, 0, 0, 1)
        }
    }
}