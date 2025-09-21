package cn.thegoodboys.murdermystery.config

import com.google.common.base.Charsets
import com.google.common.io.Files
import org.apache.commons.lang.Validate
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.configuration.file.YamlConstructor
import org.bukkit.configuration.file.YamlRepresenter
import org.bukkit.plugin.Plugin
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.representer.Representer
import java.io.*
import java.util.logging.Logger


class Config(plugin: Plugin, file: File) : YamlConfiguration() {
    protected val yamlOptions = DumperOptions()
    protected val yamlRepresenter: Representer = YamlRepresenter()
    protected val yaml = Yaml(YamlConstructor(), this.yamlRepresenter, this.yamlOptions)
    protected var file: File
    protected var loger: Logger
    protected var plugin: Plugin

    init {
        Validate.notNull(file, "File cannot be null")
        Validate.notNull(plugin, "Plugin cannot be null")
        this.plugin = plugin
        this.file = file
        loger = plugin.logger
        this.check(file)
        this.init(file)
    }


    @JvmOverloads
    constructor(plugin: Plugin, filename: String? = "config.yml") : this(plugin, File(plugin.dataFolder, filename))

    private fun check(file: File) {
        val filename = file.getName()
        val stream = plugin.getResource(filename)
        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs()
                }
                file.createNewFile()
                if (stream != null) {
                    plugin.saveResource(filename, true)
                }
            }
        } catch (e: IOException) {
            loger.info("\u914d\u7f6e\u6587\u4ef6 $filename \u521b\u5efa\u5931\u8d25...")
        }
    }

    private fun init(file: File) {
        Validate.notNull(file as Any, "File cannot be null")
        try {
            val stream = FileInputStream(file)
            this.init(stream)
        } catch (e: FileNotFoundException) {
            loger.info("\u914d\u7f6e\u6587\u4ef6 " + file.getName() + " \u4e0d\u5b58\u5728...")
        }
    }

    private fun init(stream: InputStream) {
        Validate.notNull(stream as Any, "Stream cannot be null")
        try {
            this.load(InputStreamReader(stream, Charsets.UTF_8))
        } catch (ex: IOException) {
            loger.info("\u914d\u7f6e\u6587\u4ef6 " + file.getName() + " \u8bfb\u53d6\u9519\u8bef...")
        } catch (ex: InvalidConfigurationException) {
            loger.info("\u914d\u7f6e\u6587\u4ef6 " + file.getName() + " \u683c\u5f0f\u9519\u8bef...")
        }
    }

    fun setShopLocation(locations: List<Location>, path: String?) {
        if (path == null) return

        val locationStrings = locations.map { location ->
            val world = location.world.name
            val x = location.x
            val y = location.y
            val z = location.z
            val yaw = location.yaw.toDouble()
            val pitch = location.pitch.toDouble()
            "$world,$x,$y,$z,$yaw,$pitch"
        }

        this[path] = locationStrings
    }


    fun getShopLocations(path: String): MutableList<Location> {
        val locations = mutableListOf<Location>()

        val locationStrings = this.getStringList(path)
        locationStrings.forEach { locString ->
            val parts = locString.split(",")
            if (parts.size == 6) {
                val world = Bukkit.getWorld(parts[0])
                val x = parts[1].toDouble()
                val y = parts[2].toDouble()
                val z = parts[3].toDouble()
                val yaw = parts[4].toFloat()
                val pitch = parts[5].toFloat()
                if (world != null) {
                    locations.add(Location(world, x, y, z, yaw, pitch))
                }
            }
        }
        return locations
    }

    fun setTeamShopLocation(locations: List<Location>, path: String?) {
        if (path == null) return

        val locationStrings = locations.map { location ->
            val world = location.world.name
            val x = location.x
            val y = location.y
            val z = location.z
            val yaw = location.yaw.toDouble()
            val pitch = location.pitch.toDouble()
            "$world,$x,$y,$z,$yaw,$pitch"
        }

        this[path] = locationStrings
    }

    fun getTeamShopLocations(path: String): MutableList<Location> {
        val locations = mutableListOf<Location>()

        val locationStrings = this.getStringList(path)
        locationStrings.forEach { locString ->
            val parts = locString.split(",")
            if (parts.size == 6) {
                val world = Bukkit.getWorld(parts[0])
                val x = parts[1].toDouble()
                val y = parts[2].toDouble()
                val z = parts[3].toDouble()
                val yaw = parts[4].toFloat()
                val pitch = parts[5].toFloat()
                if (world != null) {
                    locations.add(Location(world, x, y, z, yaw, pitch))
                }
            }
        }
        return locations
    }

    fun setLocations(locations: List<Location>, path: String?) {
        if (path == null) return

        val locationStrings = locations.map { location ->
            val world = location.world.name
            val x = location.x
            val y = location.y
            val z = location.z
            val yaw = location.yaw.toDouble()
            val pitch = location.pitch.toDouble()
            "$world,$x,$y,$z,$yaw,$pitch"
        }

        this[path] = locationStrings
    }

    fun getLocations(path: String): MutableList<Location> {
        val locations = mutableListOf<Location>()

        val locationStrings = this.getStringList(path)
        locationStrings.forEach { locString ->
            val parts = locString.split(",")
            if (parts.size == 6) {
                val world = Bukkit.getWorld(parts[0])
                val x = parts[1].toDouble()
                val y = parts[2].toDouble()
                val z = parts[3].toDouble()
                val yaw = parts[4].toFloat()
                val pitch = parts[5].toFloat()
                if (world != null) {
                    locations.add(Location(world, x, y, z, yaw, pitch))
                }
            }
        }
        return locations
    }

    fun setResourceLocations(locations: List<Location>, path: String?) {
        if (path == null) return

        val locationStrings = locations.map { location ->
            val world = location.world.name
            val x = location.x
            val y = location.y
            val z = location.z
            val yaw = location.yaw.toDouble()
            val pitch = location.pitch.toDouble()
            "$world,$x,$y,$z,$yaw,$pitch"
        }

        this[path] = locationStrings
    }

    fun getResourceLocations(path: String?): MutableList<Location> {
        val locations = mutableListOf<Location>()
        if (path == null) return locations

        val locationStrings = this.getStringList(path)
        locationStrings.forEach { locString ->
            val parts = locString.split(",")
            if (parts.size == 6) {
                val world = Bukkit.getWorld(parts[0])
                val x = parts[1].toDouble()
                val y = parts[2].toDouble()
                val z = parts[3].toDouble()
                val yaw = parts[4].toFloat()
                val pitch = parts[5].toFloat()
                if (world != null) {
                    locations.add(Location(world, x, y, z, yaw, pitch))
                }
            }
        }
        return locations
    }


    fun setBlockLocation(location: Location, path: String?) {
        val world = location.world.name
        val x = location.x
        val y = location.y
        val z = location.z
        this[path] = "$world,$x,$y,$z"
    }

    fun setLocation(location: Location, path: String?) {
        val world = location.world.name
        val x = location.x
        val y = location.y
        val z = location.z
        val yaw = location.yaw.toDouble()
        val pitch = location.pitch.toDouble()
        this[path] = "$world,$x,$y,$z,$yaw,$pitch"
    }

    fun getBlockLocation(path: String?): Location? {
        if (this.getString(path) != null) {
            val locationString = this.getString(path)
            val world = Bukkit.getWorld(locationString.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0])
            val x = locationString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toDouble()
            val y = locationString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2].toDouble()
            val z = locationString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[3].toDouble()
            return Location(world, x, y, z)
        }
        return null
    }

    fun getLocation(path: String?): Location? {
        if (this.getString(path) != null) {
            val locationString = this.getString(path)
            val world = Bukkit.getWorld(locationString.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0])
            val x = locationString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toDouble()
            val y = locationString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2].toDouble()
            val z = locationString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[3].toDouble()
            val yaw = locationString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[4].toDouble()
            val pitch = locationString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[5].toDouble()
            return Location(world, x, y, z, yaw.toFloat(), pitch.toFloat())
        }
        return null
    }

    @Throws(IOException::class, InvalidConfigurationException::class)
    override fun load(file: File) {
        Validate.notNull(file as Any, "File cannot be null")
        val stream = FileInputStream(file)
        this.load(InputStreamReader(stream as InputStream, Charsets.UTF_8))
    }

    @Throws(IOException::class, InvalidConfigurationException::class)
    override fun load(reader: Reader) {
        val builder = StringBuilder()
        if (reader is BufferedReader) reader else BufferedReader(reader).use { input ->
            var line: String?
            while (input.readLine().also { line = it } != null) {
                builder.append(line)
                builder.append('\n')
            }
        }
        loadFromString(builder.toString())
    }

    fun reload() {
        this.init(file)
    }

    fun save() {
        if (file == null) {
            loger.info("\u672a\u5b9a\u4e49\u914d\u7f6e\u6587\u4ef6\u8def\u5f84 \u4fdd\u5b58\u5931\u8d25!")
        }
        try {
            this.save(file)
        } catch (e: IOException) {
            loger.info("\u914d\u7f6e\u6587\u4ef6 " + file.getName() + " \u4fdd\u5b58\u9519\u8bef...")
            e.printStackTrace()
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Throws(IOException::class)
    override fun save(file: File) {
        Validate.notNull(file as Any, "File cannot be null")
        Files.createParentDirs(file)
        val data = saveToString()
        OutputStreamWriter(FileOutputStream(file) as OutputStream, Charsets.UTF_8).use { writer ->
            writer.write(
                data
            )
        }
    }

    override fun saveToString(): String {
        this.yamlOptions.indent = options().indent()
        this.yamlOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        this.yamlRepresenter.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        val header = buildHeader()
        var dump = this.yaml.dump(getValues(false) as Any)
        if (dump == "{}\n") {
            dump = ""
        }
        return header + dump
    }
}
