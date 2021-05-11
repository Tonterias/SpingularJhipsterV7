import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Notification e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookie('SESSION');
    cy.clearCookies();
    cy.intercept('GET', '/api/notifications*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('notification');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Notifications', () => {
    cy.intercept('GET', '/api/notifications*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('notification');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Notification').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Notification page', () => {
    cy.intercept('GET', '/api/notifications*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('notification');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('notification');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Notification page', () => {
    cy.intercept('GET', '/api/notifications*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('notification');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Notification');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Notification page', () => {
    cy.intercept('GET', '/api/notifications*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('notification');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Notification');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Notification', () => {
    cy.intercept('GET', '/api/notifications*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('notification');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Notification');

    cy.get(`[data-cy="creationDate"]`).type('2021-05-11T04:51').invoke('val').should('equal', '2021-05-11T04:51');


    cy.get(`[data-cy="notificationDate"]`).type('2021-05-11T08:57').invoke('val').should('equal', '2021-05-11T08:57');


    cy.get(`[data-cy="notificationReason"]`).select('AUTHORIZE_COMMUNITY_FOLLOWER');


    cy.get(`[data-cy="notificationText"]`).type('Metal USB CSS', { force: true }).invoke('val').should('match', new RegExp('Metal USB CSS'));


    cy.get(`[data-cy="isDelivered"]`).should('not.be.checked');
    cy.get(`[data-cy="isDelivered"]`).click().should('be.checked');
    cy.setFieldSelectToLastOfEntity('appuser');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/notifications*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('notification');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Notification', () => {
    cy.intercept('GET', '/api/notifications*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/notifications/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('notification');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('notification').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/notifications*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('notification');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
