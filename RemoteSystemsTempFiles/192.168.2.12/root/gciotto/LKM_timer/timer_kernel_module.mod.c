#include <linux/module.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

MODULE_INFO(vermagic, VERMAGIC_STRING);

struct module __this_module
__attribute__((section(".gnu.linkonce.this_module"))) = {
	.name = KBUILD_MODNAME,
	.init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
	.exit = cleanup_module,
#endif
	.arch = MODULE_ARCH_INIT,
};

static const struct modversion_info ____versions[]
__used
__attribute__((section("__versions"))) = {
	{ 0xae4b0ceb, "module_layout" },
	{ 0x15692c87, "param_ops_int" },
	{ 0xfe990052, "gpio_free" },
	{ 0xe61a6d2f, "gpio_unexport" },
	{ 0x6f702dba, "sock_release" },
	{ 0xf20dabd8, "free_irq" },
	{ 0xc2e4e275, "omap_dm_timer_read_counter" },
	{ 0x446225ce, "omap_dm_timer_start" },
	{ 0x35a316c4, "omap_dm_timer_set_int_enable" },
	{ 0x1a4e7f33, "omap_dm_timer_get_irq" },
	{ 0xd8a358b, "omap_dm_timer_set_load_start" },
	{ 0xb9d7449c, "omap_dm_timer_set_prescaler" },
	{ 0x2d01163b, "omap_dm_timer_free" },
	{ 0x11a2719c, "omap_dm_timer_set_source" },
	{ 0x6fe1fb3c, "omap_dm_timer_request" },
	{ 0xd6b8e852, "request_threaded_irq" },
	{ 0x11f447ce, "__gpio_to_irq" },
	{ 0x82f776b7, "gpio_export" },
	{ 0x7ffc8718, "gpio_set_debounce" },
	{ 0x65d6d0f0, "gpio_direction_input" },
	{ 0x47229b5c, "gpio_request" },
	{ 0xfa2a45e, "__memzero" },
	{ 0x1de861a2, "sock_create" },
	{ 0xefd6cf06, "__aeabi_unwind_cpp_pr0" },
	{ 0x306b7276, "sock_sendmsg" },
	{ 0x97255bdf, "strlen" },
	{ 0x91715312, "sprintf" },
	{ 0x2ed2f942, "omap_dm_timer_write_status" },
	{ 0x777c2a18, "omap_dm_timer_read_status" },
	{ 0x2e5810c6, "__aeabi_unwind_cpp_pr1" },
	{ 0x27e1a049, "printk" },
	{ 0x6c8d5ae8, "__gpio_get_value" },
};

static const char __module_depends[]
__used
__attribute__((section(".modinfo"))) =
"depends=";


MODULE_INFO(srcversion, "0B27930560A6AF297606B49");
