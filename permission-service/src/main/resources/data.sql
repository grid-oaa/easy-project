INSERT IGNORE INTO roles (id, name, code, created_at)
VALUES (1, '管理员', 'ADMIN', NOW());

INSERT IGNORE INTO permissions (id, name, service, http_method, path, created_at) VALUES
  (1, '用户列表', 'auth-service', 'GET', '/api/users', NOW()),
  (2, '创建角色', 'permission-service', 'POST', '/api/permissions/roles', NOW()),
  (3, '角色列表', 'permission-service', 'GET', '/api/permissions/roles', NOW()),
  (4, '创建权限', 'permission-service', 'POST', '/api/permissions/permissions', NOW()),
  (5, '权限列表', 'permission-service', 'GET', '/api/permissions/permissions', NOW()),
  (6, '绑定用户角色', 'permission-service', 'POST', '/api/permissions/bind/user-role', NOW()),
  (7, '绑定角色权限', 'permission-service', 'POST', '/api/permissions/bind/role-permission', NOW()),
  (8, '批量导入权限', 'permission-service', 'POST', '/api/permissions/batch', NOW()),
  (9, '菜单列表', 'permission-service', 'GET', '/api/menus', NOW()),
  (10, '菜单树', 'permission-service', 'GET', '/api/menus/tree', NOW()),
  (11, '创建菜单', 'permission-service', 'POST', '/api/menus', NOW()),
  (12, '更新菜单', 'permission-service', 'POST', '/api/menus/update', NOW()),
  (13, '删除菜单', 'permission-service', 'POST', '/api/menus/delete', NOW()),
  (14, '角色菜单列表', 'permission-service', 'GET', '/api/menus/roles', NOW()),
  (15, '绑定角色菜单', 'permission-service', 'POST', '/api/menus/roles/bind', NOW());

INSERT IGNORE INTO role_permission (role_id, permission_id) VALUES
  (1, 1), (1, 2), (1, 3), (1, 4),
  (1, 5), (1, 6), (1, 7), (1, 8),
  (1, 9), (1, 10), (1, 11), (1, 12),
  (1, 13), (1, 14), (1, 15);

INSERT IGNORE INTO user_role (user_id, role_id) VALUES (1, 1);

INSERT IGNORE INTO menus (id, parent_id, name, path, component, type, icon, sort_order, created_at) VALUES
  (1, 0, '系统管理', '/system', 'Layout', 'DIR', 'setting', 1, NOW()),
  (2, 1, '角色管理', '/system/roles', 'RoleManage', 'MENU', 'peoples', 1, NOW()),
  (3, 1, '菜单管理', '/system/menus', 'MenuManage', 'MENU', 'menu', 2, NOW());

INSERT IGNORE INTO role_menu (role_id, menu_id) VALUES
  (1, 1), (1, 2), (1, 3);
