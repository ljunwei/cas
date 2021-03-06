package org.apereo.cas.services;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Default In Memory Service Registry Dao for test/demonstration purposes.
 *
 * @author Scott Battaglia
 * @since 3.1
 */
public class InMemoryServiceRegistry implements ServiceRegistryDao {

    private List<RegisteredService> registeredServices = new ArrayList<>();

    /**
     * Instantiates a new In memory service registry.
     */
    public InMemoryServiceRegistry() {
    }

    /**
     * Instantiates a new In memory service registry dao.
     *
     * @param registeredServices the registered services
     */
    public InMemoryServiceRegistry(final List<RegisteredService> registeredServices) {
        this.registeredServices = registeredServices;
    }
    
    @Override
    public boolean delete(final RegisteredService registeredService) {
        return this.registeredServices.remove(registeredService);
    }

    @Override
    public RegisteredService findServiceById(final long id) {
        return this.registeredServices.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    @Override
    public RegisteredService findServiceById(final String id) {
        return this.registeredServices.stream().filter(r -> r.matches(id)).findFirst().orElse(null);
    }

    @Override
    public List<RegisteredService> load() {
        return this.registeredServices;
    }

    @Override
    public RegisteredService save(final RegisteredService registeredService) {
        if (registeredService.getId() == RegisteredService.INITIAL_IDENTIFIER_VALUE) {
            ((AbstractRegisteredService) registeredService).setId(findHighestId() + 1);
        }

        final RegisteredService svc = findServiceById(registeredService.getId());
        if (svc != null) {
            this.registeredServices.remove(svc);
        }
        this.registeredServices.add(registeredService);

        return registeredService;
    }

    public void setRegisteredServices(final List registeredServices) {
        this.registeredServices = ObjectUtils.defaultIfNull(registeredServices, new ArrayList<>());
    }

    /**
     * This isn't super-fast but we don't expect thousands of services.
     *
     * @return the highest service id in the list of registered services
     */
    private long findHighestId() {
        return this.registeredServices.stream().map(RegisteredService::getId).max(Comparator.naturalOrder()).orElse((long) 0);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public long size() {
        return registeredServices.size();
    }
}
