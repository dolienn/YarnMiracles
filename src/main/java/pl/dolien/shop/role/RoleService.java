package pl.dolien.shop.role;

import javax.management.relation.RoleNotFoundException;

public interface RoleService {

    Role getByName(String name) throws RoleNotFoundException;
}
