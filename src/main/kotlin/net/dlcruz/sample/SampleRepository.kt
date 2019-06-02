package net.dlcruz.sample

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SampleRepository: JpaRepository<PersistentSample, Long>